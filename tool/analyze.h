#ifndef __ANALYZE_H__
#define __ANALYZE_H__

#include <vector>

#define MAX_ACTION_NUMBER	1024

#define HEIGHT		600
#define WIDTH		1200

#define INIT_X		50

#define SQUARE_W	20
#define SQUARE_H	20

#define RIGHT_BOUNDRY	(WIDTH / 2 + SQUARE_W + 10 - SQUARE_W)
#define LEFT_BOUNDRY	(WIDTH / 2)

using namespace std;

typedef struct {
	enum {kRight, kLeft} action;
	int press_time;
	int release_time;
	int press_x;
	int release_x;
	int press_y;
	int release_y;
	int delay;
} T_ACTION;

typedef struct {
	int time;
	enum {kRightOn, kRightOff, kLeftOn, kLeftOff} action_type;
} T_LOCATION;

inline bool action_comparator(T_ACTION val1, T_ACTION val2) {
	return val1.press_time < val2.press_time;
}

inline bool loc_comparator(T_LOCATION val1, T_LOCATION val2) {
	return val1.time < val2.time;
}

void parse(FILE *input, vector<T_ACTION> &right_actions, vector<T_ACTION> &left_actions);
void merge_actions(vector<T_ACTION> &actions, vector<T_ACTION> right_actions, vector<T_ACTION> left_actions);
void parse_action(FILE *file, vector<T_ACTION> actions, vector<T_LOCATION> &locations);
void generate_location(FILE *file, vector<T_LOCATION> locations);

#endif
