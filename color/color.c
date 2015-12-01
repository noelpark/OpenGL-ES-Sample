#include <stdio.h>
 
typedef struct {
    int width;
    int height;
    unsigned char* data;
} image_t;
 
void rgb_resize_copy(image_t* source, image_t* destination);
void get_dummy_source_data(image_t* image);
void print_v (image_t* image);
void print_f (char* filename,image_t* image);

void main(){
    image_t source;
    image_t destination;
 
    source.width = 5;
    source.height = 3;
 
    destination.width = 10;
    destination.height = 10;
    destination.data = malloc(destination.width*destination.height*4);
 
    get_dummy_source_data(&source);
 
    print_v(&source);
    print_f("source.txt",&source);
 
    rgb_resize_copy(&source,&destination);    
 
    print_v(&destination);
    print_f("dest_rgb.txt",&destination);
 
    return;
}

 
void rgb_resize_copy(image_t* source, image_t* destination)
{
 
    unsigned char* src_ptr = source->data;
    unsigned char* dest_ptr = destination->data;
 
    int src_width_len = source->width * 4;
    int src_height = source->height;
    int src_index = 0;
 
    int dest_len = destination->width * destination->height * 4;
    int dest_width_len = destination->width * 4;
    int dest_width_index=0;
    int dest_height_index=0;
 
    int i=0;
 
    do {
        dest_width_index = i % dest_width_len; // width index per height
 
            // same area with source      
        if((dest_height_index < src_height) && (dest_width_index < src_width_len)) { 
            dest_ptr[dest_width_index] = src_ptr[src_index];
            src_index ++;
        } else {
            if ((dest_width_index+1)%4 == 0) //alpha
                dest_ptr[dest_width_index] = 0xFF;
            else // R,G,B
                dest_ptr[dest_width_index] = 0;
        }
 
        //The end of dest_width_index
        if (( i > 0 ) && (dest_width_index == dest_width_len-1)){
            dest_ptr+=dest_width_len;
            dest_height_index++;
        }       
 
        i++;
    } while ( i < dest_len );
}

 
void get_dummy_source_data(image_t* image){
 
    int image_len =image->width*image->height*4; 
    image->data = malloc(image_len); //RGBA
    int j =0;
    for (j=0; j < (image_len) ;j++){
        if ((j!=0) && ((j+1)%4 ==0))
            image->data[j] = 0xFF; 
        else
            image->data[j] = 1;
    } 
}
 
void print_f (char* filename,image_t* image){
   unsigned char* value = image->data;
   int width_len = image->width * 4; 
   int len = image->width *image->height* 4; 
 
    FILE *fp ;
 
    fp = fopen( filename, "w" ) ; 
    fprintf(fp,"\n=================== \n\n");
    int j =0;
    do {
        fprintf(fp,"%d ",value[j]); 
        if ((j !=0) ){
            if (((j+1))%4 == 0)
                fprintf(fp,"| "); 
            if(((j+1)%(width_len)) == 0)
                fprintf(fp,"\n"); 
        }
        j++;
 
    }while ( j < len ); 
    fprintf(fp,"\n"); 
    fclose(fp);
    return;
}
 
void print_v (image_t* image){
 
   unsigned char* value = image->data;
   int width_len = image->width * 4; 
   int len = image->width *image->height* 4; 
 
    printf("\n=================== \n\n");
    int j =0;
    do {
        printf ("%d ",value[j]); 
        if ((j !=0) ){
            if (((j+1))%4 == 0)
                printf ("| "); 
            if(((j+1)%(width_len)) == 0)
                printf ("\n"); 
        }
        j++;
 
    }while ( j < len ); 
    printf ("\n"); 
    return;
}
