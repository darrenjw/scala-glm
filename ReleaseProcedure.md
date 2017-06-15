# Release procedure

## Snapshot release

```scala
sbt +publishSigned
```

## Proper release

* Remove snapshot from version in `build.sbt`
* `sbt test package`
* Check everything OK
* `sbt +publishSigned`
* Check it seems OK
* `sbt +sonatypeRelease`
* Check it seems OK
* Bump version to next snapshot in `build.sbt`



#### eof


