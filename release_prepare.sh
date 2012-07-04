#git flow release start 1.5.2
#mvn release:prepare -DreleaseVersion=1.5.2 -Dtag=1.5.2 -DdevelopmentVersion=1.5.3-SNAPSHOT --batch-mode -DdryRun=false -Drelease
#mvn release:perform -Drelease
#git flow release finish 1.5.2
#git push origin master develop :release/1.5.2