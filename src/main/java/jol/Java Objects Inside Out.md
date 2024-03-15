doc: https://shipilev.net/jvm/objects-inside-out/


# 8. ObjectAlignment 
## ArrayList
- java -jar jol-cli.jar internals java.util.ArrayList
- java -XX:ObjectAlignmentInBytes=16 -jar jol-cli.jar internals java.util.ArrayList

## 8.1. Observation: Hiding Fields in Alignment Shadow
- java -jar jol-cli.jar internals java.lang.Object
- java -jar jol-cli.jar internals java.lang.Integer
- 