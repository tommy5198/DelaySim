set terminal png size 1200, 600
set output output_file

set grid

set xlabel "Time(ms)"

set ytics 1 nomirror
set ytics add ("" 2)
set ytics add ("" -2)
set ytics add ("" 0)
set ytics add ("Right" 1)
set ytics add ("Left" -1)
set yrange [-2:2]

set y2label "Position of X"
set y2tics
set y2range [0:1200]

set style line 1 lw 2 lc rgb "blue"
set style line 2 lw 2 lc rgb "red"

plot action_input ls 1 with lines title "Action", \
	 loc_input ls 2 with lines axes x1y2 title "Object Position", \
