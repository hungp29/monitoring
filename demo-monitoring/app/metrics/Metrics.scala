package metrics

import io.prometheus.client.{Counter, Gauge, Histogram}

object Metrics {

  val requestCount: Counter = Counter
    .build()
    .name("api_request_count")
    .help("Total number of API requests made.")
    .labelNames("func")
    .register()

  val requestDurationHistogram: Histogram = Histogram
    .build()
    .name("api_request_histogram")
    .help("Histogram of API request durations in seconds.")
    .labelNames("func", "status")
    .buckets(0.1, 0.5, 1.0, 2.5, 5.0, 10.0)
    .register()

  val activeConnections: Gauge = Gauge
    .build()
    .name("active_connections")
    .help("Number of active connections to the server")
    .register()

}
