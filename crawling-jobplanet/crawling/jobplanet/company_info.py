class CompanyInfo:
    def __init__(self, company_name, rating):
        self.company_name = company_name
        self.rating = rating

    def __str__(self):
        return f"company_name: {self.company_name}, rating: {self.rating}"
