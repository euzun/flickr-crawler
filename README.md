# Crawl and Download from Flickr
* This repo includes Flickr image crawler and downloader used in following paper.
* It does NOT need any Flickr API to work (If requested, use Flickr's public api key). It makes queries and pulls data through Flickr's public website.
* It ofcourse only crawls publicly shared photos for a given geographical bounding box.

## Usage
* Find appropriate downloader from src folder.
* Import db template file (e.g. baseDB.sql) in localhost
* Include necessary lib files into eclipse.
* Fill bounding box coordinates of the city you want to crawl, then, crawl the users who puplicly share a photo within that bounding box.
* Once users are crawled, then start downloading photos.
* Check paper for details to make filtering for your purpose.

## Citation
* Please cite to following paper(s) if you use this crawler for academic purpose;

* E. Uzun and H. T. Sencar, “[JpgScraper: An Advanced Carver for JPEG Files](https://doi.org/10.1109/TIFS.2019.2953382)”, IEEETransactions on Information Forensics and Security, 2019.

* E. Uzun and H. T. Sencar, “[Carving orphaned jpeg file fragments](https://www.researchgate.net/publication/275044127_Carving_Orphaned_JPEG_File_Fragments)”, IEEETransactions on Information Forensics and Security, vol. 10, no. 8, pp.1549–1563, 2015.
