unit uSnake;

interface

uses
  Windows, Classes, SysUtils, Forms, uMain;

procedure Game;

implementation

type
  // Летающая доска
  TFly = record
    X, Y, dX, dY, Len: integer;
  end;

var
  Level, Score: integer;
  FlyList: array of TFly;

// Двигаем доску
function MoveFly(N, Shift: integer): boolean;
var
  I, X, Y, dX, dY, Len: integer;
begin
  X := FlyList[N].X;
  Y := FlyList[N].Y;
  dX := FlyList[N].dX;
  dY := FlyList[N].dY;
  Len := FlyList[N].Len;

  // стираем доску
  for I := 0 to Len - 1 do
    Video[X + Abs(dX) * I, Y + Abs(dY) * I] := #0;

  // проверяем наличие места
  Result := True;
  X := X + dX * Shift; Y := Y + dY * Shift;
  for I := 0 to Len - 1 do
    if Video[X + Abs(dX) * I, Y + Abs(dY) * I] <> #0 then begin
      Result := False;
      break;
    end;

  // если место есть, сдвигаем доску
  if Result then begin
    FlyList[N].X := X;
    FlyList[N].Y := Y;
    FlyList[N].dX := dX * Shift;
    FlyList[N].dY := dY * Shift;
  end else begin
    X := FlyList[N].X;
    Y := FlyList[N].Y;
  end;

  // рисуем доску
  for I := 0 to Len - 1 do
    Video[X + Abs(dX) * I, Y + Abs(dY) * I] := '#';
end;

// Играть уровень
procedure PlayLevel(MouseCount, Delay: integer);
var
  Ch: char;
  MouseOk: boolean;
  I, C, R, M, dX, dY, mX, mY, Len: integer;
  pX, pY: array [1..80*25] of integer;
begin
  // начальные параметры питона
  dX := 1; dY := 0; Len := 5;

  // рисуем питона
  C := 1; R := 23;
  for I := 1 to Len do begin
    Video[C, R] := 'O';
    pX[I] := C; pY[I] := R;
    Inc(C, dX); Inc(R, dY);
  end;

  for M := MouseCount downto 1 do begin
    Video.Write(45, 0, Format('%.2d', [M]));

    // показываем мышь
    while True do begin
      mX := Random(77) + 1;
      mY := Random(22) + 1;
      if Video[mX, mY] = #0 then begin
        Video[mX, mY] := '$';
        break;
      end;
    end;

    // пока мышь не съедена
    MouseOk := False;
    while (not MouseOk) and (not Application.Terminated) do begin
      if Keyboard.KeyPressed(KEY_LEFT) then begin dX := -1; dY := 0; end;
      if Keyboard.KeyPressed(KEY_RIGHT) then begin dX := 1; dY := 0; end;
      if Keyboard.KeyPressed(KEY_UP) then begin dX := 0; dY := -1; end;
      if Keyboard.KeyPressed(KEY_DOWN) then begin dX := 0; dY := 1; end;

      // столкновение
      Ch := Video[pX[Len] + dX, pY[Len] + dY];
      if Ch <> #0 then begin
        Beep;

        // съели мышь, питон растет
        if Ch = '$' then begin
          Inc(Score, 10);
          Video.Write(75, 0, Format('%.5d', [Score]));
          Inc(Len); pX[Len] := pX[Len - 1]; pY[Len] := pY[Len - 1];
          MouseOk := True;
        end else

        // взрыв
        begin
          while Keyboard.KeyPressed do ;
          Video.Write(30, 10, '======================');
          Video.Write(30, 11, '      GAME OVER       ');
          Video.Write(30, 12, '======================');
          while not Keyboard.KeyPressed do ;
          Level := 0;
          Exit;
        end;
      end;

      // двигаем питона
      if not MouseOk then begin
        Video[pX[1], pY[1]] := #0;
        for I := 2 to Len do begin
          pX[I - 1] := pX[I]; pY[I - 1] := pY[I];
        end;
      end;
      Inc(pX[Len], dX); Inc(pY[Len], dY);
      Video[pX[Len], pY[Len]] := 'O';

      // двигаем летающие доски
      for I := 0 to High(FlyList) do
        if not MoveFly(I, 1) then MoveFly(I, -1);

      // скорость игры
      Sleep(Delay);
    end;
  end;
end;

// Создаем летающую доску
procedure CreateFly(X, Y, dX, dY, Len: integer);
var
  N: integer;
begin
  N := High(FlyList) + 1;
  SetLength(FlyList, N + 1);
  FlyList[N].X := X;
  FlyList[N].Y := Y;
  FlyList[N].dX := dX;
  FlyList[N].dY := dY;
  FlyList[N].Len := Len;

  for N := 0 to Len - 1 do
    Video[X + Abs(dX) * N, Y + Abs(dY) * N] := '#';
end;

// Игра
procedure Game;
var
  I, MouseCount, Delay: integer;
begin
  // начальные установки
  Level := 1;
  Score := 0;

  // цикл игры
  while not Application.Terminated do begin
    // очищаем экран
    Video.Cls;
    Video.Write(0, 0, 'LEVEL: 00');
    Video.Write(68, 0, 'SCORE: 00000');
    Video.Write(33, 0, 'MOUSE LEFT: 00');

    // рисуем ограждение
    for I := 0 to 79 do begin
      Video[I, 1] := 'X';
      Video[I, 24] := 'X';
    end;

    for I := 1 to 23 do begin
      Video[0, I] := 'X';
      Video[79, I] := 'X';
    end;

    // рисуем уровень
    FlyList := nil;
    MouseCount := 16; Delay := 100;
    Video.Write(7, 0, Format('%.2d', [Level]));

    case Level of
      1: begin MouseCount := 8; Delay := 150; end;
      2,3: begin
        for I := 1 to 22 do Video[I + 28, 12] := 'X';
        if Level = 3 then for I := 1 to 13 do Video[40, I + 5] := 'X';
      end;
      4: begin MouseCount := 64; end;
      5,7: begin
        CreateFly(1, 12, 1, 0, 12);
        if Level = 7 then CreateFly(40, 2, 0, 1, 3);
      end;
      6,8: begin
        CreateFly(1, 12, 1, 0, 12);
        CreateFly(66, 12, -1, 0, 12);
        for I := 1 to 5 do Video[40, I + 9] := 'X';
        for I := 11 to 13 do begin Video[39, I] := 'X'; Video[41, I] := 'X'; end;
        if Level = 8 then begin CreateFly(40, 2, 0, 1, 3); CreateFly(40, 21, 0, -1, 3); end;
      end;
      else begin
        while Keyboard.KeyPressed do ;
        Video.Write(30, 10, '======================');
        Video.Write(30, 11, '        THE END       ');
        Video.Write(30, 12, '======================');
        while not Keyboard.KeyPressed do ;
        Exit;
      end;
    end;

    // играем уровень
    PlayLevel(MouseCount, Delay);
    Inc(Level);
  end;
end;

initialization
  uMain.ScreenColCount := 80;
  uMain.ScreenRowCount := 25;
  uMain.OnStartGame := Game;
  uMain.GameCaption := 'Snake';

end.
