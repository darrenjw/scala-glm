# Makefile


FORCE:
	sbt test package

docs: FORCE
	sbt doc
	cp -r target/scala-2.12/api/* docs/
	git add docs

edit:
	emacs src/main/scala/scalaglm/*.scala src/test/scala/*.scala &



