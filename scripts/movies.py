import requests
import random
import csv

def fetch_movie_ids(api_key, target_count=1000, output_file="movie_ids.csv"):
    base_url = "https://api.themoviedb.org/3/movie/{}?api_key={}"
    checked_ids = set()

    with open(output_file, "w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(["movie_id"])  # CSV header

        while len(checked_ids) < target_count:
            movie_id = random.randint(1, 1000000)  # Random movie ID range
            if movie_id in checked_ids:
                continue

            checked_ids.add(movie_id)
            url = base_url.format(movie_id, api_key)
            response = requests.get(url)

            if response.status_code == 200:
                movie_data = response.json()
                if movie_data.get("success", True):  # Ensures valid movie data
                    writer.writerow([movie_id])  # Store only the ID
                    print(f"Fetched: {movie_data.get('title', 'Unknown')} (ID: {movie_id})")
    
    print(f"Successfully saved {len(checked_ids)} movie IDs to {output_file}")

if __name__ == "__main__":
    API_KEY = "b0af84324ea41ca39f4311c9156e94e3"
    fetch_movie_ids(API_KEY, 10000)
