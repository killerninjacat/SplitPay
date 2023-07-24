import bcrypt


def get_hashed_password(plain_text_password):
    password = plain_text_password.encode('utf-8')
    return bcrypt.hashpw(password, bcrypt.gensalt())


def verify_password(plain_text_password, hashed_password):
    password = plain_text_password.encode('utf-8')
    return bcrypt.checkpw(password, hashed_password)
