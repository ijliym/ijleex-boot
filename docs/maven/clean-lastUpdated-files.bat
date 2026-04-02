echo on
rem Delete *.lastUpdated files
rem
title Clean

cd /d %~dp0
for /r repository\ %%f in (*.lastUpdated,*.repositories,*.sha1,*.sha256,*.sha512,*.properties,*.xml) do (if exist %%f erase %%f)
for /r repository\ %%f in (unknown) do (if exist %%f rmdir /S /Q %%f)
@pause
