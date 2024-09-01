#include <stdio.h>
#include <string.h>
#include <stdlib.h>


// gcc -o hello hello.c
// ./hello


char* strdup_every_other_char(char* input) {

    if (input == NULL) {
        return NULL;
    }

    int inputLength = strlen(input);
    if (inputLength == 0) {
        return NULL;
    }

    // Add one more for the null char
    char* retStr = (char*)malloc((inputLength + 1)/2);
    if (retStr == NULL) {
        return NULL;  // Memory allocation failed
    }

    int offset = 0;
    int i = 0;

    for (int i = 0; i < inputLength; i+=2) {
        retStr[offset++] = input[i];
    }
    retStr[offset] = '\0';

    printf("%s\n", retStr);
    return retStr;
}


int main() {
    printf("Hello, World!\n");
   
    // even length
    strdup_every_other_char("cool"); //co

    // odd length
    strdup_every_other_char("c"); //c
    strdup_every_other_char("coiol"); //cil

    // empty
    strdup_every_other_char("\n"); //

     return 0;
}
