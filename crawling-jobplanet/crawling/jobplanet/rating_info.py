class RatingInfo:
    def __init__(self, company_id, rating, rating_count):
        self.company_id = company_id
        self.rating = rating
        self.rating_count = rating_count

    def __str__(self):
        return f"company_id: {self.company_id}, rating: {self.rating}, rating_count: {self.rating_count}"
