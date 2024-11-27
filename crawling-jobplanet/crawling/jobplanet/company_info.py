class CompanyInfo:
    def __init__(self, company_name, rating, rating_count, link):
        self.company_name = company_name
        self.rating = rating
        self.rating_count = rating_count
        self.link = link

    def __str__(self):
        return f"Company Name: {self.company_name}, Rating: {self.rating}, Rating Count: {self.rating_count}, Link: {self.link}"

    @staticmethod
    def from_data(company_name, rating, rating_count, link):
        return CompanyInfo(company_name, rating, rating_count, link)

    def to_dict(self):
        return {
            "company_name": self.company_name,
            "rating": self.rating,
            "rating_count": self.rating_count,
            "link": self.link
        }
