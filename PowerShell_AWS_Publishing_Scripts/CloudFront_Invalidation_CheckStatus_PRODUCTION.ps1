# Import the AWS CloudFront module
Import-Module AWS.Tools.CloudFront

# Define the CloudFront distribution ID
$distributionId = "E2L8FM1PIJHONP"

# Prompt the user for the Invalidation ID
$invalidationId = Read-Host "Enter the CloudFront Invalidation ID without """

# Function to check the status of the invalidation
function Check-InvalidationStatus {
    try {
        # Retrieve the invalidation object
        $invalidation = Get-CFInvalidation -DistributionId $distributionId -Id $invalidationId
        
        # Print the full invalidation object for debugging
        if ($invalidation) {
            Write-Host "Invalidation Object: $($invalidation | Out-String)"
            return $invalidation
        } else {
            Write-Host "No invalidation object returned. Check your parameters."
            return $null
        }
    } catch {
        Write-Host "Error retrieving invalidation object: $_"
        return $null
    }
}

# Initial status check
$invalidation = Check-InvalidationStatus

if ($invalidation -ne $null) {
    # Extract status
    $status = $invalidation.Status.Trim().ToUpper()  # Accessing Status directly from $invalidation

    Write-Host "Current Status: $status"

    # Loop until the status is 'COMPLETED'
    while ($status -ne "COMPLETED") {
        Write-Host "Invalidation is still in progress. Checking again in 30 seconds..."
        Start-Sleep -Seconds 30
        $invalidation = Check-InvalidationStatus
        
        if ($invalidation -ne $null) {
            $status = $invalidation.Status.Trim().ToUpper()  # Accessing Status directly from $invalidation
            Write-Host "Current Status: $status"
        } else {
            Write-Host "Failed to retrieve invalidation status. Exiting."
            break
        }
    }

    Write-Host "Invalidation completed successfully."
} else {
    Write-Host "Could not retrieve invalidation status."
}
