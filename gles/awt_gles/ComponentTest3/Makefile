JAVAC =  /opt/java/j2sdk1.4.2_15/bin/javac
BUILD_DIR = compTest3
JAVAFLAG = -d $(BUILD_DIR) -classpath "./$(BUILD_DIR)/"
RM = rm

.SUFFIXES:.java .class

all:
	@ rm -rf $(BUILD_DIR)
	@ mkdir -p $(BUILD_DIR)
	@ \
	    for i in java/; do \
	    $(JAVAC) $(JAVAFLAG) $$i*.java; \
	    done;
	@ tar czf compTest3.tgz compTest3
	

clean:
	@ \
	    for i in $(BUILD_DIR)/; do \
	    $(RM) $$i*.class; \
	    done;
