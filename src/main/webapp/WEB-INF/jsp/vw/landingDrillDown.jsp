<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="row">
						<table id="example" class="table table-bordered table-striped table-responsive display" style="width:100%">
							 <caption style="caption-side: top; text-align: left; font-size: 1.5em; font-weight: bold; padding: 10px;">
            Officer Details of Zone ${circleWiseCounts[0][1]}
        </caption>
							<thead style="background-color: #C0C0C0">
								<tr>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Zone Name</th>
										<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Circle Name</th>
										<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Name</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">No. of Cases</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Financial Year</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Suspected Indicative Tax Value</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Demand Created</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Amount Recovered</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Yet to be Acknowledge</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Yet to be Initiated</th>
									<th style="text-align: center; vertical-align: middle;"
										colspan="7">Case Stage</th>
								</tr>
								<tr>
									<th style="text-align: center; vertical-align: middle;">DRC-01A
										issued</th>
									<th style="text-align: center; vertical-align: middle;">ASMT-10
										issued</th>
									<th style="text-align: center; vertical-align: middle;">DRC01
										issued</th>
									<th style="text-align: center; vertical-align: middle;">Demand
										Created via DRC07</th>
									<th style="text-align: center; vertical-align: middle;">Case
										Dropped</th>
									<th style="text-align: center; vertical-align: middle;">Partial
										Voluntary Payment Remaining Demand</th>
									<th style="text-align: center; vertical-align: middle;">Demand
										Created However Discharged via DRC-03</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${circleWiseCounts}"
									var="districtWiseCounts">
									<tr>
										<td>${districtWiseCounts[1]}</td>
										<td>${districtWiseCounts[2]}</td>
										<td>${districtWiseCounts[3]} ${districtWiseCounts[4]}</td>
										<td>${districtWiseCounts[9]}</td>
										<td>${districtWiseCounts[22]}</td>
										<td>${districtWiseCounts[10]}</td>
										<td>${districtWiseCounts[11]}</td>
										<td>${districtWiseCounts[12]}</td>
										<td>${districtWiseCounts[13]}</td>
										<td>${districtWiseCounts[14]}</td>
										<td>${districtWiseCounts[15]}</td>
										<td>${districtWiseCounts[16]}</td>
										<td>${districtWiseCounts[17]}</td>
										<td>${districtWiseCounts[18]}</td>
										<td>${districtWiseCounts[19]}</td>
										<td>${districtWiseCounts[20]}</td>
										<td>${districtWiseCounts[21]}</td>

									</tr>
								</c:forEach>
							</tbody>
						</table>
										
					</div>

<script>
        $(document).ready(function() {

            $('#example').DataTable({
                dom: 'Bfrtip',
                buttons: [
                    'excel', 'pdf', 'print'
                ],
                initComplete: function(settings, json) {
                    console.log('DataTable initialized');
                }
            });
            
        });
</script>




