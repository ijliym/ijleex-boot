echo on
rem Delete *.lastUpdated files
rem
title Clean

cd /d %~dp0
for /r .\ %%f in (*.lastUpdated) do (
  if exist %%f erase %%f
)
@pause
