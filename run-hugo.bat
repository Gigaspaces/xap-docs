@echo off
call generate-navbar.bat
set HUGO_HOME=%~dp0\bin\hugo-0.17
SET HUGO_STATICDIR=../site-flare/Content/Resources/Static
%HUGO_HOME%\hugo -s site-hugo %*
call generate-canonical.bat