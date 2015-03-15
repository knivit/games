program Packman;

uses
  Forms,
  uMain in '..\uMain.pas' {fmMain},
  uPackman in 'uPackman.pas';

{$R *.res}

begin
  Application.Initialize;
  Application.CreateForm(TfmMain, fmMain);
  Application.Run;
end.
