unit uMain;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, ExtCtrls, SyncObjs, ComCtrls, ToolWin;

type
  TOnStartGame = procedure;

  // Экранная область
  TScreenBox = class(TImage)
  private
    FCharWidth, FCharHeight: integer;
    FColCount, FRowCount: integer;          // размеры экрана, символов
    FScreenBuffer: array of array of char;  // буфер экрана

    function GetScreenBuffer(Col, Row: integer): char;
    procedure SetScreenBuffer(Col, Row: integer; Value: char);
  protected
    procedure InitScreen(AColCount, ARowCount: integer);
  public
    CurCol, CurRow: integer;

    constructor Create(AOwner: TComponent); override;
    destructor Destroy; override;

    procedure Cls; overload;
    procedure Cls(X1, Y1, X2, Y2: integer; Ch: char = #0); overload;
    procedure Line(X1, Y1, X2, Y2: integer; Ch: char);
    procedure WriteChar(Ch: char);
    procedure Write(X, Y: integer; S: string);

    property ColCount: integer read FColCount;
    property RowCount: integer read FRowCount;
    property ScreenBuffer[Col, Row: integer]: char read GetScreenBuffer write SetScreenBuffer; default;
  end;

  // Клавиатура
  TKeyboardBox = class
  private
    FKeyPressed: array [0..10+26+9 - 1] of boolean;  // состояние 10 цифровых, 26 алфавитных и 8 доп. клавиш
    procedure KeyDown(Key: word; Shift: TShiftState);
    procedure KeyUp(Key: word; Shift: TShiftState);
  public
    ShiftPressed, CtrlPressed, AltPressed: boolean;
    function KeyPressed: boolean; overload;
    function KeyPressed(Key: char): boolean; overload;
    function Read(X, Y: integer; MaxLen: integer): string;
  end;

  // Форма
  TfmMain = class(TForm)
    StartTimer: TTimer;
    procedure FormCreate(Sender: TObject);
    procedure FormKeyDown(Sender: TObject; var Key: Word; Shift: TShiftState);
    procedure FormDestroy(Sender: TObject);
    procedure FormKeyUp(Sender: TObject; var Key: Word; Shift: TShiftState);
    procedure StartTimerTimer(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

const
  KEY_SPACE = #1;
  KEY_RETURN = #2;
  KEY_BACK = #3;
  KEY_TAB = #4;
  KEY_LEFT = #5;
  KEY_RIGHT = #6;
  KEY_UP = #7;
  KEY_DOWN = #8;
  KEY_ESCAPE = #9;

var
  fmMain: TfmMain;
  Video: TScreenBox;
  GameCaption: string;
  Keyboard: TKeyboardBox;
  OnStartGame: TOnStartGame;
  ScreenColCount, ScreenRowCount: integer;

implementation

{$R *.dfm}

// Экранная область
function TScreenBox.GetScreenBuffer(Col, Row: integer): char;
begin
  Assert((Col > -1) and (Col < FColCount), Format('Неверная колонка экрана: %d', [Col]));
  Assert((Row > -1) and (Row < FRowCount), Format('Неверная строка экрана: %d', [Row]));
  Result := FScreenBuffer[Col][Row];
end;

procedure TScreenBox.SetScreenBuffer(Col, Row: integer; Value: char);
var
  X, Y: integer;
begin
  Assert((Col > -1) and (Col < FColCount), Format('Неверная колонка экрана: %d', [Col]));
  Assert((Row > -1) and (Row < FRowCount), Format('Неверная строка экрана: %d', [Row]));
  FScreenBuffer[Col][Row] := Value;

  X := Col * FCharWidth;
  Y := Row * FCharHeight;
  if Value = #127 then begin
    Canvas.Brush.Color := clWhite;
    Canvas.FillRect(Rect(X, Y, X + FCharWidth, Y + FCharHeight))
  end else begin
    Canvas.Brush.Color := clBlack;
    if Value < #33 then Value := ' '; //Canvas.FillRect(Rect(X, Y, X + FCharWidth, Y + FCharHeight))
    Canvas.TextOut(X, Y, Char(Value));
  end;
  Application.ProcessMessages;
end;

procedure TScreenBox.InitScreen(AColCount, ARowCount: integer);
begin
  Assert(AColCount > 0, 'Укажите в секции initialization: uMain.ScreenColCount := <Кол-во колонок>');
  Assert(ARowCount > 0, 'Укажите в секции initialization: uMain.ScreenRowCount := <Кол-во строк>');
  FColCount := AColCount;
  FRowCount := ARowCount;
  FCharWidth := Canvas.TextWidth('X');
  FCharHeight := Canvas.TextHeight('X');
  SetLength(FScreenBuffer, FColCount, FRowCount);
  Height := ARowCount * FCharHeight;
  Width := AColCount * FCharWidth;
  Picture.Bitmap.Height := Height;
  Picture.Bitmap.Width := Width;
  Cls;
end;

constructor TScreenBox.Create(AOwner: TComponent);
begin
  inherited;

  Canvas.Brush.Color := clBlack;
  Canvas.Font.Name := 'Courier';
  Canvas.Font.Size := 10;
  Canvas.Font.Color := clWhite;
  Canvas.Brush.Style := bsClear;
end;

destructor TScreenBox.Destroy;
begin
  inherited;

  FScreenBuffer := nil;
end;

procedure TScreenBox.Cls;
var
  C, R: integer;
begin
  for R := 0 to FRowCount - 1 do
    for C := 0 to FColCount - 1 do
      FScreenBuffer[C][R] := #0;
  Canvas.Brush.Color := clBlack;
  Canvas.FillRect(Rect(0, 0, Width, Height));
  CurCol := 0; CurRow := 0;
end;

procedure TScreenBox.Cls(X1, Y1, X2, Y2: integer; Ch: char = #0);
var
  C, R: integer;
begin
  Assert((X1 >= 0) and (X2 >= 0) and (Y1 >= 0) and (Y2 >= 0) and
    (X1 < FColCount) and (X2 < FColCount) and (Y1 < FRowCount) and (Y2 < FRowCount),
    Format('Неверные координаты при вызове процедуры Cls(%d, %d, %d, %d) !', [X1, Y1, X2, Y2]));
  for R := Y1 to Y2 do
    for C := X1 to X2 do
      FScreenBuffer[C][R] := #0;
  Canvas.Brush.Color := clBlack;
  Canvas.FillRect(Rect(X1 * FCharWidth, Y1 * FCharHeight, (X2 + 1) * FCharWidth, (Y2 + 1) * FCharHeight));
end;

procedure TScreenBox.Line(X1, Y1, X2, Y2: integer; Ch: char);
var
  I: integer;
begin
  Assert((X1 >= 0) and (X2 >= 0) and (Y1 >= 0) and (Y2 >= 0) and
    (X1 < FColCount) and (X2 < FColCount) and (Y1 < FRowCount) and (Y2 < FRowCount),
    Format('Неверные координаты при вызове процедуры Line(%d, %d, %d, %d) !', [X1, Y1, X2, Y2]));
  if X1 = X2 then for I := Y1 to Y2 do Video[X1, I] := Ch
  else for I := X1 to X2 do Video[I, Y1] := Ch;
end;

procedure TScreenBox.WriteChar(Ch: char);
begin
  ScreenBuffer[CurCol, CurRow] := Ch;
  Inc(CurCol);
  if CurCol = FColCount then begin
    CurCol := 0;
    Inc(CurRow);
    if CurRow = FRowCount then CurRow := 0;
  end;
end;

procedure TScreenBox.Write(X, Y: integer; S: string);
var
  I: integer;
begin
  CurCol := X; CurRow := Y;
  for I := 1 to Length(S) do WriteChar(S[I]);
end;

// Клавиатура
procedure TKeyboardBox.KeyDown(Key: word; Shift: TShiftState);
begin
  if ssShift in Shift then ShiftPressed := True;
  if ssCtrl in Shift then CtrlPressed := True;
  if ssAlt in Shift then AltPressed := True;

  case Key of
    48..57:    FKeyPressed[Key - 48] := True;       // цифровые 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
    VK_NUMPAD0..VK_NUMPAD9: FKeyPressed[Key - VK_NUMPAD0] := True;
    65..90:    FKeyPressed[Key - 65 + 10] := True;  // алфавитные A..Z
    VK_SPACE:  FKeyPressed[36 + 0] := True;         // Space
    VK_RETURN: FKeyPressed[36 + 1] := True;         // Enter
    VK_BACK:   FKeyPressed[36 + 2] := True;         // BackSpace
    VK_TAB:    FKeyPressed[36 + 3] := True;         // Tab
    VK_LEFT:   FKeyPressed[36 + 4] := True;         // Left
    VK_RIGHT:  FKeyPressed[36 + 5] := True;         // Right
    VK_UP:     FKeyPressed[36 + 6] := True;         // Top
    VK_DOWN:   FKeyPressed[36 + 7] := True;         // Bottom
  end;
end;

procedure TKeyboardBox.KeyUp(Key: word; Shift: TShiftState);
begin
  if ssShift in Shift then ShiftPressed := False;
  if ssCtrl in Shift then CtrlPressed := False;
  if ssAlt in Shift then AltPressed := False;

  case Key of
    48..57:    FKeyPressed[Key - 48] := False;      // цифровые 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
    VK_NUMPAD0..VK_NUMPAD9: FKeyPressed[Key - VK_NUMPAD0] := False;
    65..90:    FKeyPressed[Key - 65 + 10] := False; // алфавитные A..Z
    VK_SPACE:  FKeyPressed[36 + 0] := False;        // Space
    VK_RETURN: FKeyPressed[36 + 1] := False;        // Enter
    VK_BACK:   FKeyPressed[36 + 2] := False;        // BackSpace
    VK_TAB:    FKeyPressed[36 + 3] := False;        // Tab
    VK_LEFT:   FKeyPressed[36 + 4] := False;        // Left
    VK_RIGHT:  FKeyPressed[36 + 5] := False;        // Right
    VK_UP:     FKeyPressed[36 + 6] := False;        // Top
    VK_DOWN:   FKeyPressed[36 + 7] := False;        // Bottom
    VK_ESCAPE: FKeyPressed[36 + 8] := False;        // Esc
  end;
end;

function TKeyboardBox.KeyPressed: boolean;
var
  I: integer;
begin
  Application.ProcessMessages;
  for I := 0 to High(FKeyPressed) do begin
    Result := FKeyPressed[I];
    if Result then Exit;
  end;
  Result := Application.Terminated;
end;

function TKeyboardBox.KeyPressed(Key: char): boolean;
begin
  Application.ProcessMessages;
  case Key of
    '0'..'9':       Result := FKeyPressed[Ord(Key) - 48];
    'A'..'Z':       Result := FKeyPressed[Ord(Key) - 65 + 10];
    ' ', KEY_SPACE: Result := FKeyPressed[36 + 0];
    KEY_RETURN:     Result := FKeyPressed[36 + 1];
    KEY_BACK:       Result := FKeyPressed[36 + 2];
    KEY_TAB:        Result := FKeyPressed[36 + 3];
    KEY_LEFT:       Result := FKeyPressed[36 + 4];
    KEY_RIGHT:      Result := FKeyPressed[36 + 5];
    KEY_UP:         Result := FKeyPressed[36 + 6];
    KEY_DOWN:       Result := FKeyPressed[36 + 7];
    KEY_ESCAPE:     Result := FKeyPressed[36 + 8];
    else raise Exception.CreateFmt('Неизвестная клавиша "%s"', [Key]);
  end;
end;

function TKeyboardBox.Read(X, Y: integer; MaxLen: integer): string;
var
  Key: char;
  I, N: integer;
begin
  N := 0;
  Result := '';
  while KeyPressed do ;

  while True do begin
    Video[X + N, Y] := '_';
    while not KeyPressed do ;

    if N < MaxLen then begin
      Key := #0;
      for I := Ord('0') to Ord('9') do
        if KeyPressed(Chr(I)) then Key := Chr(I);
      for I := Ord('A') to Ord('Z') do
        if KeyPressed(Chr(I)) then Key := Chr(I);
      if KeyPressed(' ') then Key := ' ';

      if Key <> #0 then begin
        Result := Result + Key;
        Video[X + N, Y] := Key;
        Inc(N);
      end;
    end;

    if KeyPressed(KEY_BACK) and (N > 0) then begin
      Result := Copy(Result, 1, N - 1);
      Video[X + N, Y] := #0;
      Dec(N);
    end;

    if KeyPressed(KEY_RETURN) or KeyPressed(KEY_ESCAPE) then begin
      if KeyPressed(KEY_ESCAPE) then Result := '';
      Video[X + N, Y] := #0;
      Exit;
    end;

    while KeyPressed do ;
  end;
end;

// Инициализация
procedure TfmMain.FormCreate(Sender: TObject);
begin
  Video := TScreenBox.Create(Self);
  Video.Parent := Self;
  Video.InitScreen(ScreenColCount, ScreenRowCount);
  Keyboard := TKeyboardBox.Create;
  if GameCaption <> '' then Caption := GameCaption;
end;

// Завершение
procedure TfmMain.FormDestroy(Sender: TObject);
begin
  Video.Free;
  Keyboard.Free;
end;

// Нажатие клавиши
procedure TfmMain.FormKeyDown(Sender: TObject; var Key: Word; Shift: TShiftState);
begin
  Keyboard.KeyDown(Key, Shift);
end;

// Отпускание клавиш
procedure TfmMain.FormKeyUp(Sender: TObject; var Key: Word; Shift: TShiftState);
begin
  Keyboard.KeyUp(Key, Shift);
end;

// Начать игру
procedure TfmMain.StartTimerTimer(Sender: TObject);
begin
  StartTimer.Enabled := False;
  if Assigned(OnStartGame) then OnStartGame
  else raise Exception.Create('В разделе initialization игрового модуля укажите uMain.OnStartGame := <Имя процедуры>');
  Close;
end;

end.
