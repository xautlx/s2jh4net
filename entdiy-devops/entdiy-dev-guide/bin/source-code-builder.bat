@echo off

set SHELL_DIR=%~dp0
echo "Using SHELL_DIR: %SHELL_DIR%"
set RUN_DIR=%cd%
echo "Using RUN_DIR: %RUN_DIR%"

cd %SHELL_DIR%/..
call mvn compile exec:java -Psource-code-builder -DbasePackages="com.entdiy;xyz.entdiy"
cd %RUN_DIR%