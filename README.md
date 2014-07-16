LocationNormalization
=====================

##Running the code
1. Go to src folder.
2. Compile with javac LocationNormalization.java
3. java LocationNormalization /input/file/path

#Approach
For each line of input, we go through the following steps:

1. Convert all characters to lower case for normalization. Although upper/lower case information can be used as a feature for identifying location entities, this feature is not fully reliable, so it is ignored for now
2. Convert the result from step 1 to a list of tokens by spliting it on non-letter characters. Because city, state names only contains letters, all other information is ignored even they may also be used as features sometimes
3. Get possible entities by checking whether consecutive tokens forms valid city, state names. This is done by checking the string against HashMaps that contain U.S. state, city names. HashMaps are used here because it provides fast lookup and the dataset is small enough to be fitted in the memory. (If spell check is to be implemented, it would be used here, e.g., if no entity is found in the list of tokens, we can get the words with similar spelling using k-gram indexes, and try those words)
4. Match possible city entity with posible state entities
5. Format the output
