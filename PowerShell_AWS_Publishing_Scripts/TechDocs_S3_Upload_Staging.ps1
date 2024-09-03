# 5/8/24  Rivka Teller & Temima Greenberg
# Script for copying from local and setting up AWS S3 Buckets Docs STAGING environment for publishing to CloudFront
# Ensure AWS CLI is installed and configured. Save the below to a .ps1 script and run in PowerShell. Example. ./Git/Publishing Scripts/TechDocs_S3_Upload_Staging.ps1
# --dryrun - preview changes without actually executing
# --exclude - exclude specific folders or files
# Run the PowerShell script - ./[name of script].ps1. Navigate to the directory** where you saved the `TechDocs_S3_Upload_Staging.ps1` or `TechDocs_S3_Upload_Production.ps1` script. - C:\Git\Publishing Scripts
   
#Define variables

 $stagingBucketName = "docs-staging.gigaspaces.com"
 $stagingBackupBucketName = "docs-staging-backup"
 $stagingLocalDirectory = "C:\Git\S3 Staging"
 $backupFolder = "backup"
 $backupFolderPath = "s3://$stagingBackupBucketName/$backupFolder/" 
 
#Configure AWS credentials in your current session
#In PowerShell run aws sso login
 
 # 1.  Copy existing files from root to /backup  

    Write-Output "Starting backup process for bucket: $stagingBackupBucketName"
  
 # 1a. Check if the backup folder exists and remove it if it does
    $existingBackup = aws s3 ls $backupFolderPath

    if ($existingBackup) {
    Write-Output "Removing existing backup folder..."
    aws s3 rm $backupFolderPath --recursive
    }
    Write-Output "Backup folder removed successfully."
	Read-Host -Prompt "Press Enter to continue. Copy content from S3 root to backup folder"
 
 # 1b  Perform the copy operation from the S3 staging root to the S3 backup folder in a different bucket excluding the folders and files that will not be updated
    Write-Output "Copying contents from the root to S3 backup bucket..."
    aws s3 cp "s3://$stagingBucketName/" $backupFolderPath --recursive --exclude "backup/*" --exclude "attachment_files/*" --exclude "download_files/*"  --exclude "google575574ad80067fc1.html" --exclude "404.html" --exclude "404-resources/*" --exclude "favicon.ico" --exclude "robots.txt" --exclude "index.html" --exclude "terms-of-use.html"
    Write-Output "Backup to backup folder completed successfully."
	Read-Host -Prompt "Press Enter to continue. Sync from local directory to s3 root"
    
 # 2.  Upload (via sync) new files from local to S3 staging root exlcuding any old Flare search files
    Write-Output "Syncing contents from the local directory to S3 staging root"
    aws s3 sync $stagingLocalDirectory "s3://$stagingBucketName/"  --exclude "*GigaSpaces-*.zip" --exclude "*/Data/SearchPhrase_*" --exclude "*/Data/SearchStem_*" --exclude "*/Data/SearchTopic_*" --exclude "*/Data/SearchUrl_*" 
    Write-Output "Sync from local to S3 root completed successfully. Now run the CloudFront Invalidation script" 
	Read-Host -Prompt "Press Enter to end"	
