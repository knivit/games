program Xonix;

uses
  Forms,
  uMain in '..\uMain.pas' {fmMain},
  uXonix in 'uXonix.pas';

{$R *.res}

begin
  Application.Initialize;
  Application.CreateForm(TfmMain, fmMain);
  Application.Run;
end.
