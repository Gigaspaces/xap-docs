# Import the AWS CloudFront module
Import-Module AWS.Tools.CloudFront

# Define the CloudFront distribution ID
#$distributionId = "arn:aws:cloudfront::573366771204:distribution/E2L8FM1PIJHONP"
$distributionId = "E2L8FM1PIJHONP"

# Define the paths you want to invalidate
$pathsToInvalidate = @(
    "/*"
   # "/path/to/file2.css",
   # "/another/path/to/file3.js"
)

# Create a unique caller reference (to ensure the request is unique)
$callerReference = [Guid]::NewGuid().ToString()

# Prepare the InvalidationBatch structure
$invalidationBatch = @{
    CallerReference = $callerReference
    Paths = @{
        Quantity = $pathsToInvalidate.Count
        Items    = $pathsToInvalidate
    }
}

# Perform the CloudFront invalidation
$invalidation = New-CFInvalidation -DistributionId $distributionId `
                                   -InvalidationBatch_CallerReference $invalidationBatch.CallerReference `
                                   -Paths_Item $invalidationBatch.Paths.Items `
                                   -Paths_Quantity $invalidationBatch.Paths.Quantity

# Output the invalidation ID and status
Write-Host "Invalidation ID: $($invalidation.Invalidation.Id)"
Write-Host "Status: $($invalidation.Invalidation.Status)"
