#!/bin/bash
out_dir=out/production/ea_nurse_rostering
cp config.properties $out_dir
cp config.properties.default $out_dir
cp -r lib $out_dir/lib
cp -r data $out_dir/data
cd $out_dir
for xml in "toy1.xml" "medium01.xml" "long01.xml"; do
    echo -n benchmarking $xml:
    declare -a absolute
    declare -a relative
    for i in {1..10}; do
        #echo - calculate iteration: $i
        echo -n .
        result=`java -cp .:lib/xom-1.2.10.jar start/Start data/$xml`
        absolute+=( $(echo $result | grep "Fitness improvement" | awk -F 'absolute: ' '{print $2}' | cut -d ',' -f1) )
        relative+=( $(echo $result | grep "Fitness improvement" | awk -F 'relative: ' '{print $2}' | cut -d '%' -f1) )
    done

    total=0
    for i in ${absolute[@]}; do
        #echo abs: $i
        total=$(echo "$total + $i" | bc)
    done
    num_abs=${#absolute[@]}
    avg_abs=$(echo "scale=4;$total / $num_abs" | bc)
    

    total=0
    for i in ${relative[@]}; do
        #echo rel: $i
        total=$(echo "$total + $i" | bc)
    done
    num_rel=${#relative[@]}
    avg_rel=$(echo "scale=4;$total / $num_rel" | bc)

    echo
    echo average absolute: $avg_abs
    echo average relative: $avg_rel%
    echo
done
