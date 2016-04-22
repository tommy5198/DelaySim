#include <cstdio>
#include <cstring>

#define MAX_SEQUENCE 1024

#define HEIGHT		600
#define WIDTH		1200

#define SQUARE_W	20
#define SQUARE_H	20

#define RIGHT_BOUNDRY	(WIDTH / 2 + SQUARE_W + 10 - SQUARE_W)
#define LEFT_BOUNDRY	(WIDTH / 2)

typedef struct {
	long long int press_time;
	long long int release_time;
	int press_x;
	int release_x;
	int press_y;
	int release_y;
	int delay;
} ENTRY;

int main()
{
	char input[1024];
	ENTRY right_seq[MAX_SEQUENCE];
	ENTRY left_seq[MAX_SEQUENCE];
	int lid_count = 0, rid_count = 0;

	fgets(input, 1024, stdin);
	printf("%s", input);

	while(fgets(input, 1024, stdin) != NULL) {
		long long int time;
		char type[16];
		char action;
		int x, y;
		int delay;
		int id;

		sscanf(input, "%lld: %s", &time, type);
		if(!strcmp(type, "Move")) {
			continue;
		}
		else {
			sscanf(input,
			       "%lld: %s %c when object at (%d, %d) with delay %d ms, id = %d"
			       , &time, type, &action, &x, &y, &delay, &id);
		}

		if(!strcmp(type, "Press")) {
			if(action == 'D') {
				right_seq[id].press_time = time;
				right_seq[id].press_x = x;
				right_seq[id].press_y = y;
				right_seq[id].delay = delay;
				rid_count++;
			}
			else {
				left_seq[id].press_time = time;
				left_seq[id].press_x = x;
				left_seq[id].press_y = y;
				left_seq[id].delay = delay;
				lid_count++;
			}
		}
		else {
			if(action == 'D') {
				right_seq[id].release_time = time;
				right_seq[id].release_x = x;
				right_seq[id].release_y = y;
			}
			else {
				left_seq[id].release_time = time;
				left_seq[id].release_x = x;
				left_seq[id].release_y = y;
			}
		}
	}

	printf("Right Actions:\n");
	for(int i = 0; i < rid_count; ++i) {
		printf("%lld ~ %lld: from (%d, %d) to (%d, %d) with delay = %d\n",
		       right_seq[i].press_time, right_seq[i].release_time,
			   right_seq[i].press_x, right_seq[i].press_y,
			   right_seq[i].release_x, right_seq[i].release_y,
			   right_seq[i].delay);

		int elapsed = right_seq[i].release_time - right_seq[i].press_time;
		int correct_packet;
		
		if(right_seq[i].press_x > RIGHT_BOUNDRY) {
			correct_packet = 0;
		}
		else {
			correct_packet = (RIGHT_BOUNDRY - right_seq[i].press_x) * 5;
			correct_packet = correct_packet < elapsed ? correct_packet : elapsed;
		}

		printf("Correct = %d, Error = %d\n", correct_packet, elapsed - correct_packet);
	}

	printf("Left Actions:\n");
	for(int i = 0; i < lid_count; ++i) {
		printf("%lld ~ %lld: from (%d, %d) to (%d, %d) with delay = %d\n",
		       left_seq[i].press_time, left_seq[i].release_time,
			   left_seq[i].press_x, left_seq[i].press_y,
			   left_seq[i].release_x, left_seq[i].release_y,
			   left_seq[i].delay);

		int elapsed = left_seq[i].release_time - left_seq[i].press_time;
		int correct_packet;

		if(left_seq[i].press_x < LEFT_BOUNDRY) {
			correct_packet = 0;
		}
		else {
			correct_packet = (left_seq[i].press_x - LEFT_BOUNDRY) * 5;
			correct_packet = correct_packet < elapsed ? correct_packet : elapsed;
		}

		printf("Correct = %d, Error = %d\n", correct_packet, elapsed - correct_packet);
	}

	return 0;
}
