unit uPackman;

interface

uses
  Windows, Forms, SysUtils, IniFiles, uMain;

implementation

type
  TDir = (drLeft, drRight, drUp, drDown);

  // Ловец
  TEnemy = record
    Ch, pCh: char;
    xDir, yDir: TDir;
    X, Y, Strategy: integer;
  end;

const
  MAXLEVEL = 1;

var
  pCh: char;
  lCh: boolean;
  EnemyList: array of TEnemy;
  nLevel, X, Y, pX, pY, Score, Life: integer;
  Level: array [0..MAXLEVEL, 1..24, 1..80] of char;

// Перечень ловцов
function IsEnemy(Ch: char): boolean;
begin
  Result := Ch in ['!', '@', '#', '%', '^', '&', '*', '(', ')'];
end;

// Очки
procedure DoScore(Off: integer);
begin
  Beep;
  Inc(Score, Off);
  pCh := ' '; lCh := True;
  Video.Write(43, 0, Format('%.5d', [Score]));
end;

// Жизни
procedure DoLife(Off: integer);
begin
  Beep;
  Inc(Life, Off);
  Video[X, Y] := ' ';
  Video.Write(78, 0, Format('%.2d', [Life]));
  while Keyboard.KeyPressed do ;
  X := pX; Y := pY;
end;

// Движение врага
procedure MoveEnemy(E: TEnemy);
  // Стратегия 1 - на сближение по X или Y
  // 3 варианта: случайно (0), по Х и по Y (1), по Y и по Х (2)
  function Strategy1(E: TEnemy; N: integer): boolean;
  var
    dX, dY: integer;
    xDir, yDir: TDir;

    // Проверка доступности знакоместа
    function CheckCh(X, Y: integer): boolean;
    begin
      Result := (Video[X, Y] <> 'X') and (not IsEnemy(Video[X, Y]));
    end;
  begin
    // находим кратчайшие смещения
    if pX <= E.X then xDir := drLeft else xDir := drRight;
    if pY <= E.Y then yDir := drUp else yDir := drDown;

    // делаем ход
    if (N = 1) or ((N = 0) and (Random(2) < 1)) then begin
      // по горизонтали
      if xDir = drLeft then begin
        // слева
        dX := -2; dY := 0;
        if not CheckCh(E.X + dX, E.Y + dY) then begin
          // по вертикали
          if yDir = drUp then begin
            // сверху
            dX := 0; dY := -1;
            if not CheckCh(E.X + dX, E.Y + dY) then begin
              // снизу
              dX := 0; dY := 1;
              if not CheckCh(E.X + dX, E.Y + dY) then begin
                // справа
                dX := 2; dY := 0;
                if not CheckCh(E.X + dX, E.Y + dY) then begin
                  dX := 0; dY := 0;
                end;
              end;
            end;
          end else begin
            // снизу
            dX := 0; dY := 1;
            if not CheckCh(E.X + dX, E.Y + dY) then begin
              // сверху
              dX := 0; dY := -1;
              if not CheckCh(E.X + dX, E.Y + dY) then begin
                // справа
                dX := 2; dY := 0;
                if not CheckCh(E.X + dX, E.Y + dY) then begin
                  dX := 0; dY := 0;
                end;
              end;
            end;
          end;
        end;
      end else begin
        // справа
        dX := 2; dY := 0;
        if not CheckCh(E.X + dX, E.Y + dY) then begin
          // по вертикали
          if yDir = drUp then begin
            // сверху
            dX := 0; dY := -1;
            if not CheckCh(E.X + dX, E.Y + dY) then begin
              // снизу
              dX := 0; dY := 1;
              if not CheckCh(E.X + dX, E.Y + dY) then begin
                // слева
                dX := -2; dY := 0;
                if not CheckCh(E.X + dX, E.Y + dY) then begin
                  dX := 0; dY := 0;
                end;
              end;
            end;
          end else begin
            // снизу
            dX := 0; dY := 1;
            if not CheckCh(E.X + dX, E.Y + dY) then begin
              // сверху
              dX := 0; dY := -1;
              if not CheckCh(E.X + dX, E.Y + dY) then begin
                // слева
                dX := -2; dY := 0;
                if not CheckCh(E.X + dX, E.Y + dY) then begin
                  dX := 0; dY := 0;
                end;
              end;
            end;
          end;
        end;
      end
    end else begin
      // по вертикали
      if yDir = drUp then begin
        // сверху
        dX := 0; dY := -1;
        if not CheckCh(E.X + dX, E.Y + dY) then begin
          // по горизонтали
          if xDir = drLeft then begin
            // слева
            dX := -2; dY := 0;
            if not CheckCh(E.X + dX, E.Y + dY) then begin
              // справа
              dX := 2; dY := 0;
              if not CheckCh(E.X + dX, E.Y + dY) then begin
                // снизу
                dX := 0; dY := 1;
                if not CheckCh(E.X + dX, E.Y + dY) then begin
                  dX := 0; dY := 0;
                end;
              end;
            end;
          end else begin
            // справа
            dX := 2; dY := 0;
            if not CheckCh(E.X + dX, E.Y + dY) then begin
              // слева
              dX := -2; dY := 0;
              if not CheckCh(E.X + dX, E.Y + dY) then begin
                // снизу
                dX := 0; dY := 1;
                if not CheckCh(E.X + dX, E.Y + dY) then begin
                  dX := 0; dY := 0;
                end;
              end;
            end;
          end;
        end;
      end else begin
        // снизу
        dX := 0; dY := 1;
        if not CheckCh(E.X + dX, E.Y + dY) then begin
          // по горизонтали
          if xDir = drLeft then begin
            // слева
            dX := -2; dY := 0;
            if not CheckCh(E.X + dX, E.Y + dY) then begin
              // справа
              dX := 2; dY := 0;
              if not CheckCh(E.X + dX, E.Y + dY) then begin
                // сверху
                dX := 0; dY := -1;
                if not CheckCh(E.X + dX, E.Y + dY) then begin
                  dX := 0; dY := 0;
                end;
              end;
            end;
          end else begin
            // справа
            dX := 2; dY := 0;
            if not CheckCh(E.X + dX, E.Y + dY) then begin
              // слева
              dX := -2; dY := 0;
              if not CheckCh(E.X + dX, E.Y + dY) then begin
                // сверху
                dX := 0; dY := -1;
                if not CheckCh(E.X + dX, E.Y + dY) then begin
                  dX := 0; dY := 0;
                end;
              end;
            end;
          end;
        end;
      end;
    end;

    // перемещаем
    Result := False;
    if (dX <> 0) or (dY <> 0) then begin
      Video[E.X, E.Y] := E.pCh;
      Inc(E.X, dX); Inc(E.Y, dY);
      E.pCh := Video[E.X, E.Y];
      Result := E.pCh = #127;
      Video[E.X, E.Y] := E.Ch;
    end;
  end;
begin
  if Strategy1(E, E.Strategy) then DoLife(-1);
end;

// Цикл игры
procedure PlayLevel;
var
  Ch: char;
  Found: boolean;
  I, C, R, dX, dY: integer;

  // Поиск указанного символа
  function FindChInVideo(Ch: char; var X, Y: integer): boolean;
  var
    C, R: integer;
  begin
    Result := False;
    for R := 1 to 24 do
      for C := 0 to 79 do
        if Video[C, R] = Ch then begin
          X := C; Y := R;
          Result := True;
          Exit;
        end;
  end;
begin
  // стартовые координаты игрока
  X := pX; Y := pY;
  while (Life > 0) and (not Application.Terminated) do begin
    dX := 0; dY := 0;
    if Keyboard.KeyPressed(KEY_LEFT) then if X > 0 then begin dX := -2; dY := 0; end;
    if Keyboard.KeyPressed(KEY_RIGHT) then if X < 79 then begin dX := 2; dY := 0; end;
    if Keyboard.KeyPressed(KEY_UP) then if Y > 1 then begin dX := 0; dY := -1; end;
    if Keyboard.KeyPressed(KEY_DOWN) then if Y < 24 then begin dX := 0; dY := 1; end;

    // движение игрока
    if (dX <> 0) or (dY <> 0) then begin
      // стираем игрока
      Video[X, Y] := pCh; lCh := False;
      while True do begin
        Ch := Video[X + dX, Y + dY];

        // столкновение с врагом
        if IsEnemy(Ch) then begin
          DoLife(-1);
          Exit;
        end;

        // набираем очки
        if Ch = '$' then DoScore(20);
        if Ch = '.' then DoScore(1);

        // непроходимое препятствие
        if Ch = 'X' then begin dX := 0; dY := 0; end;

        // телепортеры
        if Ch < #32 then begin
          Video[X + dX, Y + dY] := ' ';
          if FindChInVideo(Ch, C, R) then begin
            Video[X + dX, Y + dY] := Ch;
            X := C; Y := R;
            continue;
          end;
        end;

        break;
      end;

      // рисуем игрока
      Inc(X, dX); Inc(Y, dY);
      if not lCh then pCh := Video[X, Y];
      Video[X, Y] := #127;

      // ништяки кончились ? да - выходим
      Found := False;
      for R := 1 to 24 do
        for C := 0 to 79 do
          if Video[C, R]= '.' then begin
            Found := True;
            break;
          end;
      if not Found then begin
        DoLife(1);
        Inc(nLevel);
        Exit;
      end;
    end;

    // движение ловцов
    for I := 0 to High(EnemyList) do
      MoveEnemy(EnemyList[I]);

    // скорость игры
    Sleep(100);
  end;
end;

// Игра
procedure Game;
var
  Ch: char;
  S: string;
  F: TextFile;
  I, C, R, L, N: integer;
begin
  // загружаем уровни
  S := ChangeFileExt(ParamStr(0), '.dat');
  AssignFile(F, S);
  Reset(F);
  L := -1; R := 1;
  while L <= MAXLEVEL do begin
    Readln(F, S);
    if Copy(S, 1, 1) = '[' then begin
      Inc(L);
      R := 1;
      continue;
    end;

    for I := 1 to 80 do Level[L, R, I] := #0;
    N := (80 - Length(S)) div 2;
    for I := 1 to Length(S) do Level[L, R, I + N] := S[I];
    if Eof(F) then break;
    Inc(R);
  end;
  CloseFile(F);

  // цикл игры
  nLevel := 1; Score := 0; Life := 5;
  while not Application.Terminated do begin
    Video.Cls;
    Video.Write(0, 0, Format('LEVEL: %.2d', [nLevel]));
    Video.Write(36, 0, Format('SCORE: %.5d', [Score]));
    Video.Write(71, 0, Format('LIFE: %.2d', [Life]));

    // рисуем уровень
    // определяем координаты "ловцов", игрока и телепортеров
    if nLevel > MAXLEVEL then nLevel := 1;
    for R := 1 to 24 do
      for C := 0 to 79 do begin
        Ch := Level[nLevel, R, C + 1];
        Video[C, R] := Ch;
        if IsEnemy(Ch) then begin
          N := High(EnemyList) + 1;
          SetLength(EnemyList, N + 1);
          EnemyList[N].X := C;
          EnemyList[N].Y := R;
          EnemyList[N].Ch := Ch;
          EnemyList[N].pCh := ' ';
          EnemyList[N].Strategy := Random(3);
        end else
        if Ch = #127 then begin
          pX := C; pY := R; pCh := ' ';
        end;
      end;

    // уровень игры
    PlayLevel;

    // скорость игры
    Sleep(150);
  end;
end;

initialization
  uMain.ScreenColCount := 80;
  uMain.ScreenRowCount := 25;
  uMain.OnStartGame := Game;
  uMain.GameCaption := 'Packman';

end.
