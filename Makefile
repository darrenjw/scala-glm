# Makefile

SCALA_VERSION=3.3.0


FORCE:
	sbt test package

docs: FORCE
	sbt doc mdoc
	cp -r target/scala-$(SCALA_VERSION)/api/* docs/
	git add docs/*

edit:
	emacs *.md Makefile build.sbt mdoc/*.md src/main/scala/scalaglm/*.scala src/test/scala/*.scala examples/src/main/scala/*.scala &

todo:
	grep TODO: src/main/scala/scalaglm/*.scala

commit:
	git commit -a && git push && git pull && git log | head -20

# eof
