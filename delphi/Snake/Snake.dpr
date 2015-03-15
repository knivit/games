program Snake;

uses
  Forms,
  uMain in '..\uMain.pas' {fmMain},
  uSnake in 'uSnake.pas';

{$R *.res}

begin
  Application.Initialize;
  Application.CreateForm(TfmMain, fmMain);
  Application.Run;
end.
