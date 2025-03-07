import mysql.connector
import requests
import random
import json

# Database credentials
DB_HOST = "localhost"
DB_NAME = "ratingsdb"
DB_USER = "root"
DB_PASS = "ayman123"

# TMDB API Key
API_KEY = "b0af84324ea41ca39f4311c9156e94e3"

# Fetch movies from TMDB
def fetch_movies(api_key, target_count=1000):
    base_url = "https://api.themoviedb.org/3/movie/{}?api_key={}"
    movies = []
    checked_ids = set()
    
    while len(movies) < target_count:
        movie_id = random.randint(1, 1000000)
        if movie_id in checked_ids:
            continue

        checked_ids.add(movie_id)
        url = base_url.format(movie_id, api_key)
        response = requests.get(url)

        if response.status_code == 200:
            movie_data = response.json()
            if movie_data.get("success", True):  # Ensure valid movie data
                movies.append(movie_data["id"])
                print(f"Fetched: {movie_data.get('title', 'Unknown')} (ID: {movie_data['id']})")
    
    return movies

# Insert ratings into the database
def insert_ratings(movies):
    try:
        conn = mysql.connector.connect(
            host=DB_HOST,
            user=DB_USER,
            password=DB_PASS,
            database=DB_NAME
        )
        cursor = conn.cursor()

        insert_query = """
        INSERT IGNORE INTO ratings (user_id, movie_id, rating)
        VALUES (%s, %s, %s)
        """

        ratings_data = []
        for movie_id in movies:
            for user_id in range(1, 101):  # Users 1 to 100
                rating = random.randint(1, 100)  # Random rating from 1 to 100
                ratings_data.append((user_id, movie_id, rating))

        cursor.executemany(insert_query, ratings_data)
        conn.commit()
        print(f"Inserted {len(ratings_data)} ratings successfully.")

    except mysql.connector.Error as err:
        print("Error:", err)
    finally:
        cursor.close()
        conn.close()

if __name__ == "__main__":
    movies = fetch_movies(API_KEY, target_count=1000)
    insert_ratings(movies)
