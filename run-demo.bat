REM Copyright (C) 2012 United States Government as represented by the Administrator of the
REM National Aeronautics and Space Administration.
REM All Rights Reserved.

REM Windows Batch file for Running a WorldWind Demo
REM $Id: run-demo.bat 1171 2013-02-11 21:45:02Z dcollins $

@echo Running %1
java -Xmx512m -Dsun.java2d.noddraw=true -classpath .\src;.\classes;.\worldwind.jar;.\worldwindx.jar;.\jogl-all.jar;.\gluegen-rt.jar;.\gdal.jar;.\plugin.jar;.\lib-external\h2\h2-1.3.174.jar;.\lib-external\hibernate\jpa\hibernate-entitymanager-4.2.6.Final.jar;.\lib-external\hibernate\required\antlr-2.7.7.jar;.\lib-external\hibernate\required\dom4j-1.6.1.jar;.\lib-external\hibernate\required\hibernate-commons-annotations-4.0.2.Final.jar;.\lib-external\hibernate\required\hibernate-core-4.2.6.Final.jar;.\lib-external\hibernate\required\hibernate-jpa-2.0-api-1.0.1.Final.jar;.\lib-external\hibernate\required\javassist-3.15.0-GA.jar;.\lib-external\hibernate\required\jboss-logging-3.1.0.GA.jar;.\lib-external\hibernate\required\jboss-transaction-api_1.1_spec-1.0.1.Final.jar;.\lib-external\jgoodies-forms\jgoodies-forms-1.7.1.jar;.\lib-external\jgoodies-forms\forms_rt.jar;.\lib-external\jgoodies-animation\jgoodies-animation-1.4.3.jar;.\lib-external\jgoodies-binding\jgoodies-binding-2.9.1.jar;.\lib-external\jgoodies-common\jgoodies-common-1.6.0.jar;.\lib-external\jgoodies-looks\jgoodies-looks-2.5.3.jar;.\lib-external\jgoodies-validation\jgoodies-validation-2.5.0.jar;.\lib-external\scala\compiler\scala-compiler.jar;.\lib-external\scala\compiler\scala-library.jar;.\lib-external\scala\compiler\scala-reflect.jar;.\lib-external\scala\lib\akka-actors.jar;.\lib-external\scala\lib\diffutils.jar;.\lib-external\scala\lib\jline.jar;.\lib-external\scala\lib\scala-actors-migration.jar;.\lib-external\scala\lib\scala-actors.jar;.\lib-external\scala\lib\scala-partest.jar;.\lib-external\scala\lib\scala-swing.jar;.\lib-external\scala\lib\scalap.jar;.\lib-external\scala\lib\typesafe-config.jar; ua.edu.odeku.ceem.mapRadar.CeemRadarApplicationTemplate



