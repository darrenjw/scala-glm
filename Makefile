# Makefile


FORCE:
	sbt test package

edit:
	emacs src/main/scala/scalaglm/*.scala src/test/scala/*.scala &



