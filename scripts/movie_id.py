import requests
import random
import time
import json

def fetch_movies(api_key, target_count=1000):
    base_url = "https://api.themoviedb.org/3/movie/{}?api_key={}"
    movies = []
    checked_ids = set()
    
    while len(movies) < target_count:
        movie_id = random.randint(1, 1000000)  # Random movie ID range
        if movie_id in checked_ids:
            continue
        
        checked_ids.add(movie_id)
        url = base_url.format(movie_id, api_key)
        response = requests.get(url)
        
        if response.status_code == 200:
            movie_data = response.json()
            if movie_data.get("success", True):  # Ensures valid movie data
                movies.append(movie_data)
                print(f"Fetched: {movie_data.get('title', 'Unknown')} (ID: {movie_id})")
        
        #time.sleep(0.2)  # Rate-limiting to avoid API blocks
    
    return movies

def save_movies_to_file(movies, filename="movies.json"):
    with open(filename, "w", encoding="utf-8") as f:
        json.dump(movies, f, indent=4)
    print(f"Movies saved to {filename}")

if __name__ == "__main__":
    API_KEY = "b0af84324ea41ca39f4311c9156e94e3"
    movies = fetch_movies(API_KEY)
    save_movies_to_file(movies)
    print(f"Successfully fetched {len(movies)} movies!")
