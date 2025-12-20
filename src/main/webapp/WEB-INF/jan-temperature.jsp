<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Jan's Temperature Calculator</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container my-4">
    <h1>Jan's Temperature Calculator</h1>
    <p class="lead">Enter a temperature value and select scales to convert.</p>

    <form method="POST" action="jan-temperature">
        <div class="row align-items-center">

            <div class="col-md-3">
                <div class="form-group mb-2">
                    <label class="form-label">Temperature</label>
                    <input type="text" class="form-control" placeholder="Enter value" name="degrees" value="${degrees}">
                </div>
                <div class="text-danger small">
                    ${degreesError}
                </div>
            </div>

            <div class="col-md-3">
                <div class="form-group mb-2">
                    <label class="form-label">From</label>
                    <select class="form-select" name="currentScale">
                        <option <c:if test="${currentScale eq 'C'}">selected</c:if> value="C">Celsius (°C)</option>
                        <option <c:if test="${currentScale eq 'F'}">selected</c:if> value="F">Fahrenheit (°F)</option>
                        <option <c:if test="${currentScale eq 'K'}">selected</c:if> value="K">Kelvin (K)</option>
                    </select>
                </div>
                <div class="text-danger small">
                    ${currentScaleError}
                </div>
            </div>

            <div class="col-md-1 text-center fs-3 pt-3">
                <span>&rarr;</span>
            </div>

            <div class="col-md-3">
                <div class="form-group mb-2">
                    <label class="form-label">To</label>
                    <select class="form-select" name="targetScale">
                        <option <c:if test="${targetScale eq 'C'}">selected</c:if> value="C">Celsius (°C)</option>
                        <option <c:if test="${targetScale eq 'F'}">selected</c:if> value="F">Fahrenheit (°F)</option>
                        <option <c:if test="${targetScale eq 'K'}">selected</c:if> value="K">Kelvin (K)</option>
                    </select>
                </div>
                <div class="text-danger small">
                    ${targetScaleError}
                </div>
            </div>

            <div class="col-md-2 pt-3">
                <button type="submit" class="btn btn-primary w-100">Convert</button>
            </div>
        </div>
    </form>

    <div class="mt-4">
        <c:if test="${not empty result}">
            <div class="alert alert-success fs-4">
                Result: ${result}
            </div>
        </c:if>
    </div>

    <div class="mt-5 border-top pt-3">
        <a href="fraction" class="btn btn-outline-secondary btn-sm">Switch to Fraction Calculator</a>
    </div>

</div>
</body>
</html>