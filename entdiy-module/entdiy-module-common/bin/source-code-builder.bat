@echo off
set SHELL_DIR=%~dp0
echo "Using SHELL_DIR: %SHELL_DIR%"
set RUN_DIR=%cd%
echo "Using RUN_DIR: %RUN_DIR%"

cd %SHELL_DIR%/../../../entdiy-devops/entdiy-dev-codebuilder/bin
./source-code-builder.sh %SHELL_DIR%/.. com.entdiy

cd %RUN_DIR%