<html>
<head>
<title>Jenkins Automation Report</title>

<style type="text/css">

.ok { font-weight: bold; color: green;}
.bad { font-weight: bold; color: red;}
.passed { color: green; text-align: center;}
.failed { color: red; text-align: center; }
.cell {text-align: center; }
</style>

</head>

<body>

<#list builds as build>
<h3>Run #${build_index + 1} Details</h3>

<table border="1" style="width: 100%">
	<tr>
		<td><b>Status:</b> ${TemplateUtil.getBuildStatus(build.run.status)}</td>
		<td><b>Run Type:</b> RND Sanity</td>
		<td><b>Total Pass:</b> ${build.run.totalPass}%</td>
	</tr>
	<tr>
		<td><b>Device Version:</b> 30.1</td>
		<td><b>Total Duration:</b> ${TemplateUtil.formatDuration(build.run.time)}</td>
		<td><b>Start time:</b> ${build.run.timeStamp}</td>
	</tr>
	<tr>
		<td><b>Build:</b> ${build.run.deviceInfo.deviceBuild}</td>
		<td><b>Platform:</b> ${build.run.deviceInfo.devicePlatform}</td>
		<td><b>Device Type:</b> ${build.run.deviceInfo.deviceType}</td>
	</tr>
	<tr>
		<td><b>Device IP:</b> ${build.run.deviceInfo.deviceIp}</td>
		<td><b>Runner IP:</b> ${build.run.runnerIp}</td>
		<td><b>Full Report:</b> <a href="${TemplateUtil.getReportLink(build.run.runnerIp,build.build.number,build.jobName)}">Link</a></td>
	</tr>

	<th>Test</th>
	<th>Status</th>
	<th>Bugs</th>
	<#list build.run.testList as test>
	<tr>
		<td>${test.name} (${test.ipVersion})</td>
		<td class="${test.status?string("passed","failed")}">${test.status?string("PASSED", "FAILED")}</td>
		<td>${TemplateUtil.getFancyBugDisplay(test.bugList)}</td>
	</tr>
	</#list>
</table>

</#list>
</body>
</html>