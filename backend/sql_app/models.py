from sqlalchemy import Boolean, Column, ForeignKey, Integer, String
from sqlalchemy.orm import relationship

from .database import Base


class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True, index=True)
    email = Column(String, unique=True, index=True)
    hashed_password = Column(String)
    is_active = Column(Boolean, default=True)

    transactions = relationship('Transaction', back_populates='owner')
    splits = relationship('Split', back_populates='owner')


class Transaction(Base):
    __tablename__ = 'transactions'

    id = Column(Integer, primary_key=True, index=True)
    title = Column(String, index=True)
    description = Column(Integer, index=True)
    settled = Column(Boolean, index=True)
    owner_id = Column(Integer, ForeignKey('users.id'))

    owner = relationship('User', back_populates='transactions')


class Split(Base):
    __tablename__ = 'splits'

    id = Column(Integer, primary_key=True, index=True)
    title = Column(String, index=True)
    names = Column(String,index=True)
    description = Column(Integer, index=True)
    owner_id = Column(Integer, ForeignKey('users.id'))

    owner = relationship('User', back_populates='splits')    
