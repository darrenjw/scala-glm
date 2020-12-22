# Makefile


FORCE:
	sbt test package

docs: FORCE
	sbt doc mdoc
	cp -r target/scala-2.12/api/* docs/api/
	git add docs/api

edit:
	emacs src/main/scala/scalaglm/*.scala src/test/scala/*.scala examples/src/main/scala/*.scala &



