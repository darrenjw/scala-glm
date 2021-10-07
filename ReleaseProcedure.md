# Release procedure

## Snapshot release

* `sbt +test`
* `sbt +package`
* `sbt +publishSigned`
* Check it seems OK: https://oss.sonatype.org/content/repositories/snapshots/com/github/darrenjw/


## Proper release

* Check ReleaseNotes OK
* Remove snapshot from version in `build.sbt`
* `sbt test package`
* Check everything OK
* `sbt +publishSigned`
* Check it seems OK
* `sbt +sonatypeRelease`
* Check it seems OK: https://oss.sonatype.org/content/repositories/releases/com/github/darrenjw/
* Check/edit ReadMe version numbers
* Bump version numbers for examples and example scripts and re-run to check
* Commit and push
* Check GitHub OK
* Do a GitHub release
* Bump version to next snapshot in `build.sbt`



#### eof


