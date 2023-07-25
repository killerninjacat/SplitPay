from sqlalchemy.orm import Session

from . import models, schemas
from .helper import *


def get_user(db: Session, user_id: int):
    return db.query(models.User).filter(models.User.id == user_id).first()


def get_user_by_email(db: Session, email: str):
    return db.query(models.User).filter(models.User.email == email).first()


def get_users(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.User).offset(skip).limit(limit).all()


def create_user(db: Session, user: schemas.UserCreate):
    hashed_password = get_hashed_password(plain_text_password=user.password)
    db_user = models.User(email=user.email, hashed_password=hashed_password)
    db.add(db_user)
    db.commit()
    db.refresh(db_user)

    return db_user


def get_transactions(db: Session, user_id: int, skip: int = 0, limit: int = 100):
    return db.query(models.Transaction).filter_by(owner_id=user_id).offset(skip).limit(limit).all()

def get_transaction(db: Session, user_id: int,transaction_id: int, skip: int = 0, limit: int = 100):
    return db.query(models.Transaction).filter_by(owner_id=user_id, id=transaction_id).offset(skip).limit(limit).first()


def create_user_transaction(db: Session, transaction: schemas.TransactionCreate, user_id: int):
    db_transaction = models.Transaction(**transaction.dict(), owner_id=user_id)
    db.add(db_transaction)
    db.commit()
    db.refresh(db_transaction)

    return db_transaction


def get_splits(db: Session, user_id: int, skip: int = 0, limit: int = 100):
    return db.query(models.Split).filter_by(owner_id=user_id).offset(skip).limit(limit).all()



def create_user_split(db: Session, split: schemas.SplitCreate, user_id: int):
    db_split = models.Split(**split.dict(), owner_id=user_id)
    db.add(db_split)
    db.commit()
    db.refresh(db_split)

    return db_split