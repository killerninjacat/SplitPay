from typing import List

from fastapi import Depends, FastAPI, HTTPException
from sqlalchemy.orm import Session

from . import models, schemas
from . import crud, helper
from .database import SessionLocal, engine

models.Base.metadata.create_all(bind=engine)

app = FastAPI()


# Dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


@app.post('/new-user/', response_model=schemas.User)
def create_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    db_user = crud.get_user_by_email(db, email=user.email)

    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered.")

    return crud.create_user(db=db, user=user)


@app.get('/users/', response_model=List[schemas.User])
def read_users(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    users = crud.get_users(db, skip=skip, limit=limit)

    return users


@app.get('/users/{user_id}', response_model=schemas.User)
def read_user(user_id: int, db: Session = Depends(get_db)):
    db_user = crud.get_user(db, user_id=user_id)

    if not db_user:
        raise HTTPException(status_code=404, detail="User not found.")

    return db_user


@app.post('/users/{user_id}/new-transaction/', response_model=schemas.Transaction)
def create_transaction_for_user(user_id: int, transaction: schemas.TransactionCreate, db: Session = Depends(get_db)):
    return crud.create_user_transaction(db=db, transaction=transaction, user_id=user_id)


@app.get('/users/{user_id}/transactions/', response_model=List[schemas.Transaction])
def read_transactions(user_id: int, skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    transactions = crud.get_transactions(db, skip=skip, limit=limit, user_id=user_id)

    return transactions

@app.post('/users/{user_id}/new-split/', response_model=schemas.Split)
def create_split_for_user(user_id: int, split: schemas.SplitCreate, db: Session = Depends(get_db)):
    return crud.create_user_split(db=db, split=split, user_id=user_id)


@app.get('/users/{user_id}/splits/', response_model=List[schemas.Split])
def read_splits(user_id: int, skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    splits = crud.get_splits(db, skip=skip, limit=limit, user_id=user_id)

    return splits


@app.post('/verify-password/', response_model=schemas.VerificationResponse)
def verify_password(verification: schemas.VerificationRequest, db: Session = Depends(get_db)):
    user = crud.get_user_by_email(db, email=verification.email)

    if not user:
        raise HTTPException(status_code=404, detail="User not found.")

    password_correct = helper.verify_password(verification.password, user.hashed_password)

    return schemas.VerificationResponse(password_correct=password_correct)

