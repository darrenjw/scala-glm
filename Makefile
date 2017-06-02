# Makefile


FORCE:
	sbt test package

docs:
	sbt doc
	cp -r target/scala-2.12/api/* doc/
	git add doc

edit:
	emacs src/main/scala/scalaglm/*.scala src/test/scala/*.scala &



