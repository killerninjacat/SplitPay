from typing import List, Optional
from pydantic import BaseModel


class TransactionBase(BaseModel):
    title: str
    description: int
    settled: bool

class TransactionCreate(TransactionBase):
    pass


class Transaction(TransactionBase):
    id: int
    owner_id: int

    class Config:
        orm_mode = True

class SplitBase(BaseModel):
    title: str
    description: int

class SplitCreate(SplitBase):
    pass


class Split(SplitBase):
    id: int
    owner_id: int

    class Config:
        orm_mode = True        

class VerificationResponse(BaseModel):
    password_correct: bool

class VerificationRequest(BaseModel):
    email: str
    password: str
    
class UserBase(BaseModel):
    email: str


class UserCreate(UserBase):
    password: str


class User(UserBase):
    id: int
    is_active: bool
    transactions: List[Transaction] = []

    class Config:
        orm_mode = True