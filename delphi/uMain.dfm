object fmMain: TfmMain
  Left = 193
  Top = 114
  Width = 521
  Height = 307
  AutoSize = True
  BorderIcons = [biSystemMenu, biMinimize]
  Caption = 'The Game'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  OnCreate = FormCreate
  OnDestroy = FormDestroy
  OnKeyDown = FormKeyDown
  OnKeyUp = FormKeyUp
  PixelsPerInch = 96
  TextHeight = 13
  object StartTimer: TTimer
    Interval = 300
    OnTimer = StartTimerTimer
    Left = 16
    Top = 16
  end
end
