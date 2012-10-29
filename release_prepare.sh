#STANDARD VERSION
git checkout 2.x
git checkout -b release/2.0.0
mvn release:prepare -DreleaseVersion=2.0.0 -Dtag=2.0.0 -DdevelopmentVersion=2.0.1-SNAPSHOT --batch-mode -DdryRun=false -Drelease
mvn release:perform -Drelease
git checkout 2.x
git merge --no-ff release/2.0.0

git checkout release/2.0.0
git reset --hard HEAD~1
git push --force origin release/2.0.0
git checkout 2.x

# finally, if & when the code gets deployed to production
git checkout master
git merge --no-ff release/2.0.0
git branch -d release/2.0.0
git push