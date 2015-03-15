unit uXonix;

interface

uses
  Windows, Forms, SysUtils, IniFiles, uMain;

implementation

type
  TDir = (diLeftUp, diRightUp, diLeftBottom, diRightBottom);

  TFly = record
    X, Y: integer;
    Dir: TDir;
  end;

var
  InHit: boolean;
  OList, SList: array of TFly;
  uX, uY, Level, Life, Score: integer;

// Заменяем один символ на другой
// Возвращаем количество замен
function Replace(Src, Dest: char): integer;
var
  X, Y: integer;
begin
  Result := 0;
  for Y := 1 to 24 do
    for X := 0 to 79 do
      if Video[X, Y] = Src then begin
        Video[X, Y] := Dest;
        Inc(Result);
      end;
end;

// Заполнение захваченных областей
procedure FillArea;
const
  FILLED = #31;
  NOTFILLED = #30;
var
  X, Y: integer;

  function Empty(X, Y: integer): boolean;
  var
    C: integer;
  begin
    Result := True;
    if (X < 0) or (X > 79) or (Y < 1) or (Y > 24) then Exit;

    // слева
    C := X;
    while (C > 0) and (Video[C, Y] in [#0, 'O']) do begin
      if Video[C, Y] = #0 then Video[C, Y] := FILLED else Result := False;
      if (Video[C, Y - 1] = #0) and (not Empty(C, Y - 1)) then Result := False;
      if (Video[C, Y + 1] = #0) and (not Empty(C, Y + 1)) then Result := False;
      Dec(C);
    end;

    // справа
    C := X + 1;
    while (C < 80) and (Video[C, Y] in [#0, 'O']) do begin
      if Video[C, Y] = #0 then Video[C, Y] := FILLED else Result := False;
      if (Video[C, Y - 1] = #0) and (not Empty(C, Y - 1)) then Result := False;
      if (Video[C, Y + 1] = #0) and (not Empty(C, Y + 1)) then Result := False;
      Inc(C);
    end;
  end;
begin
  for Y := 1 to 24 do
    for X := 0 to 79 do
      if Video[X, Y] = #0 then
        if Empty(X, Y) then Score := Score + Replace(FILLED, 'X') else Replace(FILLED, NOTFILLED);
  Replace(NOTFILLED, #0);
  Video.Write(41, 0, Format('%.5d', [Score]));
end;

// Подсчет количества символов на экране
function CalcChCount(Ch: char): integer;
var
  X, Y: integer;
begin
  Result := 0;
  for Y := 1 to 24 do
    for X := 0 to 79 do
      if Video[X, Y] = Ch then Inc(Result);
end;

// Уменьшаем кол-во попыток
procedure EndLife;
begin
  Beep;
  Dec(Life);
  Replace('+', #0);
  while Keyboard.KeyPressed do ;
  Video.Write(78, 0, Format('%.2d', [Life]));
  if InHit then Video[uX, uY] := #0 else Video[uX, uY] := 'X';

  InHit := False;
  uX := 40; uY := 24;
  Video[uX, uY] := #127;
end;

// Смещение объектов
procedure Shift(var OList: array of TFly; OOn, OOff: char);
var
  Ch: char;
  Dir: TDir;
  Moved: boolean;
  I, N, X, Y, dX, dY: integer;

  // Эффект отражения
  procedure ChangeDir(X, Y: integer; var Dir: TDir);
  var
    Ch1, Ch2: char;

    function GetCh(X, Y: integer): char;
    begin
      Result := #0;
      if (X > -1) and (X < 80) and (Y > 0) and (Y < 25) then Result := Video[X, Y];
    end;
  begin
    case Dir of
      diLeftUp: begin
        Ch1 := GetCh(X, Y + 1); Ch2 := GetCh(X + 1, Y);
        if Ch1 = OOff then Dir := diLeftBottom else
        if Ch2 = OOff then Dir := diRightUp
        else Dir := diRightBottom;
      end;
      diRightUp: begin
        Ch1 := GetCh(X - 1, Y); Ch2 := GetCh(X, Y + 1);
        if Ch1 = OOff then Dir := diLeftUp else
        if Ch2 = OOff then Dir := diRightBottom
        else Dir := diLeftBottom;
      end;
      diLeftBottom: begin
        Ch1 := GetCh(X + 1, Y); Ch2 := GetCh(X, Y - 1);
        if Ch1 = OOff then Dir := diRightBottom else
        if Ch2 = OOff then Dir := diLeftUp
        else Dir := diRightUp;
      end;
      diRightBottom: begin
        Ch1 := GetCh(X, Y - 1); Ch2 := GetCh(X - 1, Y);
        if Ch1 = OOff then Dir := diRightUp else
        if Ch2 = OOff then Dir := diLeftBottom
        else Dir := diLeftUp;
      end;
    end;
  end;
begin
  for I := 0 to High(OList) do begin
    X := OList[I].X;
    Y := OList[I].Y;
    Dir := OList[I].Dir;

    // стираем объект
    Video[X, Y] := OOff;

    // двигаем объект (проверяем все 4 направления)
    for N := 1 to 4 do begin
      dX := 0; dY := 0;
      case Dir of
        diLeftUp: begin dX := -1; dY := -1; end;
        diRightUp: begin dX := 1; dY := -1; end;
        diLeftBottom: begin dX := -1; dY := 1; end;
        diRightBottom: begin dX := 1; dY := 1; end;
      end;

      if (dX <> 0) or (dY <> 0) then begin
        if ((X + dX) > -1) and ((X + dX) < 80) and ((Y + dY) > 0) and ((Y + dY) < 25) then Ch := Video[X + dX, Y + dY]
        else Ch := #0;

        // столкновение с игроком
        if ((OOn = 'O') and (Ch in [#127, '+']) and InHit) or ((OOn = ' ') and (Ch in [#127]) and (not InHit)) then EndLife;

        // отражение объекта
        Moved := True;
        if ((OOn = 'O') and (Ch in [' ', #127, 'O', 'X'])) or ((OOn = ' ') and (Ch in [#0, '+', #127, 'O'])) then begin
          ChangeDir(X + dX, Y + dY, Dir);
          Moved := False;
        end;

        // при возможности смещаем объект
        if Moved then begin
          OList[I].X := X + dX;
          OList[I].Y := Y + dY;
          OList[I].Dir := Dir;
          break;
        end;
      end else ChangeDir(X, Y, Dir);
    end;

    // рисуем объект
    Video[OList[I].X, OList[I].Y] := OOn;
  end;
end;

// Сеанс игры
procedure PlayLevel;
var
  Ch: char;
  dX, dY: integer;
begin
  InHit := False;
  while (not Application.Terminated) and (Life > 0) do begin
    dX := 0; dY := 0;
    if Keyboard.KeyPressed(KEY_LEFT) then if uX > 0 then begin dX := -1; dY := 0; end;
    if Keyboard.KeyPressed(KEY_RIGHT) then if uX < 79 then begin dX := 1; dY := 0; end;
    if Keyboard.KeyPressed(KEY_UP) then if uY > 1 then begin dX := 0; dY := -1; end;
    if Keyboard.KeyPressed(KEY_DOWN) then if uY < 24 then begin dX := 0; dY := 1; end;

    // движение игрока
    if (dX <> 0) or (dY <> 0) then begin
      // стираем игрока
      if InHit then Video[uX, uY] := '+'
      else Video[uX, uY] := 'X';

      // берем место
      Ch := Video[uX + dX, uY + dY];
      if Ch in [#0, 'X'] then begin
        if Ch = #0 then InHit := True else
        if InHit then begin
          InHit := False;
          Score := Score + Replace('+', 'X');
          FillArea;
          if CalcChCount(#0) < 256 then begin
            Inc(Level);
            Inc(Life);
            Exit;
          end;
        end;

        // рисуем игрока
        uX := uX + dX; uY := uY + dY;
        Video[uX, uY] := #127;
      end else EndLife;
    end;

    // сдвигаем объекты
    Shift(OList, 'O', #0);
    Shift(SList, ' ', 'X');

    // задержка
    Sleep(100);
  end;

  // попытки кончились, начинаем игру сначала
  Level := 1; Life := 5; Score := 0;
end;

// Игра
procedure Game;
var
  Dir: TDir;
  I, X, Y: integer;
begin
  // начальные установки
  Randomize;
  Level := 1; Life := 5; Score := 0;

  // цикл игры
  while not Application.Terminated do begin
    // очищаем экран
    Video.Cls;
    Video.Write(0, 0, Format('LEVEL: %.2d', [Level]));
    Video.Write(72, 0, Format('LIFE: %.2d', [Life]));
    Video.Write(34, 0, Format('SCORE: %.5d', [Score]));

    // рисуем ограждение
    for I := 0 to 79 do begin
      Video[I, 1] := 'X'; Video[I, 2] := 'X';
      Video[I, 24] := 'X'; Video[I, 23] := 'X';
    end;

    for I := 1 to 23 do begin
      Video[0, I] := 'X'; Video[1, I] := 'X';
      Video[79, I] := 'X'; Video[78, I] := 'X';
    end;

    // рисуем игрока
    uX := 40; uY := 24;
    Video[uX, uY] := #127;

    // выставляем фишки 'O'
    SetLength(OList, Level + 1);
    for I := 0 to Level do begin
      repeat
        X := Random(78) + 1;
        Y := Random(23) + 1;
        if Random(2) = 0 then if Random(2) = 0 then Dir := diLeftUp else Dir := diRightUp
        else if Random(2) = 0 then Dir := diLeftBottom else Dir := diRightBottom;
      until Video[X, Y] = #0;
      Video[X, Y] := 'O';

      OList[I].X := X;
      OList[I].Y := Y;
      OList[I].Dir := Dir;
    end;

    // выставляем фишки ' '
    SetLength(SList, Level + 1);
    for I := 0 to Level do begin
      repeat
        X := Random(80);
        Y := Random(24) + 1;
        if Random(2) = 0 then if Random(2) = 0 then Dir := diLeftUp else Dir := diRightUp
        else if Random(2) = 0 then Dir := diLeftBottom else Dir := diRightBottom;
        case Dir of
          diLeftUp: X := 0;
          diRightUp: X := 79;
          diLeftBottom: Y := 1;
          diRightBottom: Y := 24;
        end;
      until Video[X, Y] = 'X';
      Video[X, Y] := ' ';

      SList[I].X := X;
      SList[I].Y := Y;
      SList[I].Dir := Dir;
    end;

    // игра
    PlayLevel;
  end;
end;

initialization
  uMain.ScreenColCount := 80;
  uMain.ScreenRowCount := 25;
  uMain.OnStartGame := Game;
  uMain.GameCaption := 'Xonix';

end.
