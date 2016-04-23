#include <algorithm>
#include <cstdio>
#include <cstring>

#include "analyze.h"

using namespace std;

int main(int argc, char *argv[])
{
	vector<T_ACTION> actions;
	vector<T_ACTION> right_actions;
	vector<T_ACTION> left_actions;
	vector<T_LOCATION> locations;

	if(argc != 4) {
		printf("usage: ./analyze input_file action_file location_file\n");

		return 1;
	}

	// Preserve the space for actions
	right_actions.reserve(MAX_ACTION_NUMBER);
	left_actions.reserve(MAX_ACTION_NUMBER);

	FILE *file;
	file = fopen(argv[1], "r");
	parse(file, right_actions, left_actions);
	fclose(file);

	merge_actions(actions, right_actions, left_actions);
	sort(actions.begin(), actions.end(), action_comparator);

	file = fopen(argv[2], "w");;
	parse_action(file, actions, locations);
	fclose(file);

	file = fopen(argv[3], "w");
	generate_location(file, locations);
	fclose(file);

	return 0;
}

void parse(FILE *input_file, vector<T_ACTION> &right_actions,
           vector<T_ACTION> &left_actions)
{
	char input[1024];
	long long int start_time;
	bool first_flag = false; // used for catching start time

	fgets(input, 1024, input_file); // Discard the first line

	while(fgets(input, 1024, input_file) != NULL) {
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

		if(!first_flag) {
			start_time = time;
			first_flag = true;
		}

		if(!strcmp(type, "Press")) {
			if(action == 'D') {
				if(right_actions.size() <= id) {
					right_actions.resize(id + 1);
				}

				right_actions[id].action = T_ACTION::kRight;
				right_actions[id].press_time = time - start_time;
				right_actions[id].press_x = x;
				right_actions[id].press_y = y;
				right_actions[id].delay = delay;
			}
			else {
				if(left_actions.size() <= id) {
					left_actions.resize(id + 1);
				}

				left_actions[id].action = T_ACTION::kLeft;
				left_actions[id].press_time = time - start_time;
				left_actions[id].press_x = x;
				left_actions[id].press_y = y;
				left_actions[id].delay = delay;
			}
		}
		else {
			if(action == 'D') {
				right_actions[id].release_time = time - start_time;
				right_actions[id].release_x = x;
				right_actions[id].release_y = y;
			}
			else {
				left_actions[id].release_time = time - start_time;
				left_actions[id].release_x = x;
				left_actions[id].release_y = y;
			}
		}
	}
}

void merge_actions(vector<T_ACTION> &actions, vector<T_ACTION> right_actions,
                   vector<T_ACTION> left_actions)
{
	for(int i = 0; i < right_actions.size(); ++i) {
		actions.push_back(right_actions[i]);
	}
	for(int i = 0; i < left_actions.size(); ++i) {
		actions.push_back(left_actions[i]);
	}
}

void parse_action(FILE *file, vector<T_ACTION> actions,
                  vector<T_LOCATION> &locations)
{
	if(actions.size() == 0) {
		return;
	}

	fprintf(file, "# x y\n");
	for(int i = 0; i < actions.size(); ++i) {
		T_LOCATION press_loc, release_loc;
		int press_time = actions[i].press_time;
		int release_time = actions[i].release_time;
		int delay = actions[i].delay;

		press_loc.time = press_time + delay;
		release_loc.time = release_time + delay;
		if(actions[i].action == T_ACTION::kRight) {
			fprintf(file, "%d 0\n%d 1\n%d 1\n%d 0\n",
			        press_time, press_time, release_time, release_time);
			printf("%d ~ %d: Move right from (%d, %d) to (%d, %d) with delay = %d\n",
				   actions[i].press_time, actions[i].release_time,
				   actions[i].press_x, actions[i].press_y,
				   actions[i].release_x, actions[i].release_y,
				   actions[i].delay);

			press_loc.action_type = T_LOCATION::kRightOn;
			release_loc.action_type = T_LOCATION::kRightOff;
		}
		else if(actions[i].action == T_ACTION::kLeft) {
			fprintf(file, "%d 0\n%d -1\n%d -1\n%d 0\n",
			        press_time, press_time, release_time, release_time);
			printf("%d ~ %d: Move left from (%d, %d) to (%d, %d) with delay = %d\n",
				   actions[i].press_time, actions[i].release_time,
				   actions[i].press_x, actions[i].press_y,
				   actions[i].release_x, actions[i].release_y,
				   actions[i].delay);

			press_loc.action_type = T_LOCATION::kLeftOn;
			release_loc.action_type = T_LOCATION::kLeftOff;
		}
		else {
			printf("Incorrect action type\n");
		}

		locations.push_back(press_loc);
		locations.push_back(release_loc);

		int elapsed = release_time - press_time;
		int correct_packet;
		
		if(actions[i].press_x > RIGHT_BOUNDRY) {
			correct_packet = 0;
		}
		else {
			correct_packet = (RIGHT_BOUNDRY - actions[i].press_x) * 5;
			correct_packet = correct_packet < elapsed ? correct_packet : elapsed;
		}

		printf("Correct = %d, Error = %d\n", correct_packet, elapsed - correct_packet);
	}
}

void generate_location(FILE *file, vector<T_LOCATION> locations)
{
	int loc_x = INIT_X;

	fprintf(file, "0 %d\n", loc_x);

	sort(locations.begin(), locations.end(), loc_comparator);

	for(int i = 0; i < locations.size() - 1; ++i) {
		fprintf(file, "%d %d\n", locations[i].time, loc_x);
		int elapsed = locations[i + 1].time - locations[i].time;
		switch(locations[i].action_type) {
			case T_LOCATION::kRightOn:
				loc_x += elapsed / 5;
				break;
			case T_LOCATION::kLeftOn:
				loc_x -= elapsed / 5;
				break;
			case T_LOCATION::kRightOff:
			case T_LOCATION::kLeftOff:
				break;
		}
		fprintf(file, "%d %d\n", locations[i + 1].time, loc_x);	
	}
}
