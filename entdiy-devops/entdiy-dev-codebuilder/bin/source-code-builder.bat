@echo off

set SHELL_DIR=%~dp0
echo "Using SHELL_DIR: %SHELL_DIR%"
set RUN_DIR=%cd%
echo "Using RUN_DIR: %RUN_DIR%"

echo Rebuild codebuilder...
cd %SHELL_DIR%/..
mvn clean install

echo Generate template files...
cd %1
mvn clean compile exec:java -Psource-code-builder -DbasePackages="%2"