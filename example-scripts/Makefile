# Makefile
# Stuff for turning simple scala scripts into worksheet sessions for including into latex or markdown documents...

FILES=quickstart.ws LinearRegression.ws LogisticRegression.ws

SCRIPTDIR=./scripts

FORCE:
	make $(FILES)

%.log: $(SCRIPTDIR)/%.scala
	sbt console < $< > $@

%.ws: %.log
	tail -n +9 $< | sed -r '/^scala> |^     \|/! s/^/\/\/ /g' | sed 's/^scala> //g' | sed 's/^     | //g' | sed '/^\/\/ $$/d' | sed '/^:quit/d' > $@


clean:
	rm -rf *~ *.log $(FILES)


# eof


