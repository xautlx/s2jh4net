@echo off

set SHELL_DIR=%~dp0
echo "Using SHELL_DIR: %SHELL_DIR%"
set RUN_DIR=%cd%
echo "Using RUN_DIR: %RUN_DIR%"

rem 当前项目模块包前缀
set basePackages=xyz.entdiy.shop

rem 注意根据实际目录层次结构做必要修改指向entdiy-dev-codebuilder项目目录
cd %SHELL_DIR%/../../entdiy-devops/entdiy-dev-codebuilder/bin

./source-code-builder.bat %SHELL_DIR%/.. %basePackages%