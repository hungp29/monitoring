import metrics.MetricsFilter
import play.api.http.DefaultHttpFilters
import play.filters.csrf.CSRFFilter
import play.filters.gzip.GzipFilter

import javax.inject.Inject

class Filters @Inject() (
    gzipFilter: GzipFilter,
    csrfFilter: CSRFFilter,
    metricsFilter: MetricsFilter
) extends DefaultHttpFilters(metricsFilter, gzipFilter, csrfFilter)
