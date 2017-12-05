@echo off
set runDir=%cd%
set batDir=%~dp0
cd %batDir%/..
call mvn compile exec:java -Psource-code-builder -DbasePackages=com.entdiy.schedule
cd %runDir%