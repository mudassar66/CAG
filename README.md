# CAG
CAG is an efficient stream processing system that efficiently maintains load balance and efficiently provides a solution to consistent partitioning and minimizes aggregation cost. CAG limits the partition of data and processes the DAG in one node. We reduce the partition of data, so that stream processing's overall computation and aggregation cost remain low. The results of the experiment using large-scale real-world datasets show that CAG achieves a 3.5× improvement in terms of processing throughput and reduces the latency by 97% compared to state-of-the-art design. 

# Introduction
Current distributed stream processing systems like Storm, Flink, Samze, S4, etc. primary objective is to process data with high throughput at low latency. The architecture of these systems has a scale-out architecture to process an immense volume of data with a continuous data stream. Shuffle grouping , Key grouping, and partial key grouping are the important stream partitioning schemes use in these systems. Shuffle grouping sends tuples to  in a roundrobin style. The shuffle grouping is mostly used for stateless operators because when it is used for stateful operators, it an cause scalability issues in terms of memory. Additionally, communication cost increases when data is partitioned uniformly across operator instances. Shuffle grouping efficiently manages load balance. However, due to the scalability and heavy aggregation cost, it is not preferred for stateful operators. Key grouping uses for stateful operators. In this scheme, all the tuples with the same keys are pro-cessed on the same operator instance, making it memory- efficient. Such a scheme can raise load imbalance issue across multiple workers. The load imbalance issue is acute in the presence of a skewed stream. However, such a scheme does not need an additional aggre- gation step. 

We examine the performance of shuffle and partial key grouping to investigate how aggregation cost can cause the performance degradation of the distributed stream processing systems in an experiment. We observe the average processing time and average aggregation time of the tuples in different parallelism levels for these grouping schemes. The highest aggregation time of shuffle grouping is 70% of the processing time. For partial key grouping, the highest aggregation time is 84% of the processing time. We can see that aggregation time for partial key grouping increases with the parallelism levels from the results as shown in the following figure. From the above results, we can see that aggregation time for partial key grouping increases with the parallelism levels. It is evident from these results is that the aggregation cost is a root cause of the scalability issue for both shuffle and partial key grouping.

![Aggregation cost](https://github.com/mudassar66/CAG/blob/main/images/aggregation_cost.png?raw=true)

We ran another experiment to examine the performance of partial key grouping in greater detail to highlight the improtance of consistent partitioning i.e., the throughput or latency values are close to each other when a similar tuple input rate is applied to the system. The result in following figure shows that based on worker processes positions, the partitioning scheme performs differently. We observe that the performance of the second configuration is always lower than the first configuration. This scenario exhibits that partial key grouping is performing inconsistent partitioning.  

![Consistent partitioning](https://github.com/mudassar66/CAG/blob/main/images/consistency.png?raw=true)

Based on the above analysis, we find that aggregation cost and inconsistent stream partitioning scheme are two significant problems for the performance degradation of the DSPS. The aggregation cost affects the performance with the increase in DAG levels. Additionally, the inconsistent partitioning can utilize the physical resources inefficiently. It leads to a problem where some resources are used more than their capacity, and some resources remain idle.  Based on these insights, we propose a consistent and aggregation cost-aware efficient stream partitioning scheme called CAG (Consistent and Aggregation cost-aware Grouping). CAG partitions data from upstream OIs to downstream OIs so that overall communication overheads are reduced, and resources are used efficiently. With such an arrangement, the aggregation cost is reduced, and a consistent stream partitioning is attained.


CAG designs a threshold detector component for identifying the downstream operator instance threshold. The detector identifies the threshold of the downstream operator instance using performance modeling before the actual processing. We further design a stream partitioning component that is responsible for the necessary partitioning of data among downstream operator instances. The following contributions: 1) demonstrate that along with poor load balancing, inconsistent partitioning, and aggregation cost are crucial factors to reduce stream processing efficiency for stateful applications when stream distribution is skewed; 2) propose a cost model for stream partitioning that considers the communication cost and reduction of key splitting for stateful operations; 3) we introduce CAG, an efficient stream partitioning scheme which efficiently solves load balancing issue with consistent partitioning and minimizes the aggregation cost; 4) we measure the impact of CAG on Apache Storm and find that CAG achieves an up to 3.5x improvement in terms of throughput and reduces latency up to 97% as compared to state-of-the-art design. 


# Architecture of CAG

![CAG architecture](https://github.com/mudassar66/CAG/blob/main/images/cag_main.png?raw=true)

CAG consists of two components: 1) a threshold detecting component and 2) a stream partitioning component. 

The threshold detecting component is a standalone component that analyzes the tuple count, and based on the specific streaming application, returns the threshold value for that streaming application. 

The stream partitioning component maintains a global hash-based array that collects information of tuples for each specific task. 

# How to use

## Environment


Use the package manager [pip](https://pip.pypa.io/en/stable/) to install foobar.

```bash
pip install foobar
```

## Usage


```python
import foobar

foobar.pluralize('word') # returns 'words'
foobar.pluralize('goose') # returns 'geese'
foobar.singularize('phenomena') # returns 'phenomenon'
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
